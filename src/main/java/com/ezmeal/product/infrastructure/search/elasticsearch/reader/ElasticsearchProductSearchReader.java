package com.ezmeal.product.infrastructure.search.elasticsearch.reader;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.ezmeal.common.exception.CustomException;
import com.ezmeal.product.application.request.ProductSearchRequest;
import com.ezmeal.product.application.search.ProductSearchReader;
import com.ezmeal.product.application.search.ProductSearchResult;
import com.ezmeal.product.domain.exception.ProductErrorCode;
import com.ezmeal.product.infrastructure.search.elasticsearch.document.ProductSearchDocument;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchProductSearchReader implements ProductSearchReader {

    private final ElasticsearchOperations elasticsearchOperations;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 50;
    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("createdAt", "modifiedAt", "price", "name");

    @Override
    public Page<ProductSearchResult> search(ProductSearchRequest request) {
        int page = resolvePage(request.page());
        int size = resolveSize(request.size());
        String sortBy = resolveSortBy(request.sortBy());
        Sort.Direction direction = resolveDirection(request.direction());

        validatePriceRange(request.minPrice(), request.maxPrice());
        validateArrivalTimeRange(request);

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        NativeQuery query = buildQuery(request, pageRequest);

        long start = System.currentTimeMillis();

        SearchHits<ProductSearchDocument> searchHits =
                elasticsearchOperations.search(query, ProductSearchDocument.class);

        log.info("product search es elapsed={}ms", System.currentTimeMillis() - start);

        List<ProductSearchResult> content = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(this::toResult)
                .toList();

        return new PageImpl<>(content, pageRequest, searchHits.getTotalHits());
    }

    private NativeQuery buildQuery(ProductSearchRequest request, PageRequest pageRequest) {
        NativeQueryBuilder queryBuilder = NativeQuery.builder()
                .withPageable(pageRequest);

        List<Query> filters = new ArrayList<>();

        if (request.category() != null && !request.category().isBlank()) {
            filters.add(termQuery("category", request.category()));
        }

        if (request.mealPeriod() != null && !request.mealPeriod().isBlank()) {
            filters.add(termQuery("mealPeriod", request.mealPeriod()));
        }

        if (request.dayOfWeek() != null && !request.dayOfWeek().isBlank()) {
            filters.add(termQuery("availableDays", request.dayOfWeek()));
        }

        if (hasDeliveryAreaCondition(request)) {
            filters.add(deliveryAreaNestedQuery(request));
        }

        if (request.available() != null) {
            filters.add(termQuery("available", request.available()));
        }

        if (request.minPrice() != null || request.maxPrice() != null) {
            filters.add(priceRangeQuery(request.minPrice(), request.maxPrice()));
        }

        if ((request.keyword() == null || request.keyword().isBlank()) && filters.isEmpty()) {
            queryBuilder.withQuery(q -> q.matchAll(m -> m));
        } else if (request.keyword() != null && !request.keyword().isBlank()) {
            queryBuilder.withQuery(q -> q.bool(b -> {
                b.must(m -> m.multiMatch(mm -> mm
                        .query(request.keyword())
                        .fields("name", "description", "companyName", "menuNames")
                ));

                filters.forEach(filter -> b.filter(filter));
                return b;
            }));
        } else {
            queryBuilder.withQuery(q -> q.bool(b -> {
                filters.forEach(filter -> b.filter(filter));
                return b;
            }));
        }


        return queryBuilder.build();
    }

    private Query deliveryAreaNestedQuery(ProductSearchRequest request) {
        Integer arrivalStartMinute = toMinute(request.arrivalStartTime());
        Integer arrivalEndMinute = toMinute(request.arrivalEndTime());

        return Query.of(q -> q
                .nested(n -> n
                        .path("deliveryAreas")
                        .query(nq -> nq
                                .bool(b -> {
                                    if (hasText(request.region())) {
                                        b.filter(termQuery("deliveryAreas.region", request.region()));
                                    }

                                    if (hasText(request.mealPeriod())) {
                                        b.filter(termQuery("deliveryAreas.mealPeriod", request.mealPeriod()));
                                    }

                                    if (arrivalEndMinute != null) {
                                        b.filter(rangeLteQuery(
                                                "deliveryAreas.estimatedArrivalStartMinute",
                                                arrivalEndMinute
                                        ));
                                    }

                                    if (arrivalStartMinute != null) {
                                        b.filter(rangeGteQuery(
                                                "deliveryAreas.estimatedArrivalEndMinute",
                                                arrivalStartMinute
                                        ));
                                    }

                                    return b;
                                })
                        )
                )
        );
    }

    private Query termQuery(String field, String value) {
        return Query.of(q -> q
                .term(t -> t
                        .field(field)
                        .value(value)
                )
        );
    }

    private Query termQuery(String field, Boolean value) {
        return Query.of(q -> q
                .term(t -> t
                        .field(field)
                        .value(value)
                )
        );
    }

    private Query priceRangeQuery(Integer minPrice, Integer maxPrice) {
        return Query.of(q -> q
                .range(r -> r
                        .number(n -> {
                            n.field("price");

                            if (minPrice != null) {
                                n.gte((double) minPrice);
                            }

                            if (maxPrice != null) {
                                n.lte((double) maxPrice);
                            }

                            return n;
                        })
                )
        );
    }


    private ProductSearchResult toResult(ProductSearchDocument document) {
        return new ProductSearchResult(
                document.getProductId(),
                document.getCompanyId(),
                document.getCompanyName(),
                document.getName(),
                document.getDescription(),
                document.getPrice(),
                document.getMaxOrderCount(),
                document.getAvailable(),
                document.getCategory(),
                document.getMealPeriod(),
                document.getAvailableDays(),
                document.getDeliveryRegions(),
                document.getCreatedAt(),
                document.getModifiedAt()
        );
    }

    private int resolvePage(Integer page) {
        if (page == null || page < 0) {
            return DEFAULT_PAGE;
        }

        return page;
    }

    private int resolveSize(Integer size) {
        if (size == null || size <= 0) {
            return DEFAULT_SIZE;
        }

        return Math.min(size, MAX_SIZE);
    }

    private String resolveSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return DEFAULT_SORT_BY;
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return DEFAULT_SORT_BY;
        }

        return sortBy;
    }

    private Sort.Direction resolveDirection(String direction) {
        if (direction == null || direction.isBlank()) {
            return DEFAULT_DIRECTION;
        }

        try {
            return Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            return DEFAULT_DIRECTION;
        }
    }

    private void validatePriceRange(Integer minPrice, Integer maxPrice) {
        if (minPrice != null && minPrice < 0) {
            throw new CustomException(ProductErrorCode.PRODUCT_INVALID_REQUEST);
        }

        if (maxPrice != null && maxPrice < 0) {
            throw new CustomException(ProductErrorCode.PRODUCT_INVALID_REQUEST);
        }

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new CustomException(ProductErrorCode.PRODUCT_INVALID_REQUEST);
        }
    }

    private boolean hasDeliveryAreaCondition(ProductSearchRequest request) {
        return hasText(request.region())
                || hasText(request.arrivalStartTime())
                || hasText(request.arrivalEndTime());
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private Integer toMinute(String time) {
        if (!hasText(time)) {
            return null;
        }

        try {
            java.time.LocalTime localTime = java.time.LocalTime.parse(time);
            return localTime.getHour() * 60 + localTime.getMinute();
        } catch (java.time.format.DateTimeParseException e) {
            throw new CustomException(ProductErrorCode.PRODUCT_INVALID_REQUEST);
        }
    }

    private void validateArrivalTimeRange(ProductSearchRequest request) {
        Integer start = toMinute(request.arrivalStartTime());
        Integer end = toMinute(request.arrivalEndTime());

        if (start != null && end != null && start > end) {
            throw new CustomException(ProductErrorCode.PRODUCT_INVALID_REQUEST);
        }
    }

    private Query rangeLteQuery(String field, Integer value) {
        return Query.of(q -> q
                .range(r -> r
                        .number(n -> n
                                .field(field)
                                .lte(value.doubleValue())
                        )
                )
        );
    }

    private Query rangeGteQuery(String field, Integer value) {
        return Query.of(q -> q
                .range(r -> r
                        .number(n -> n
                                .field(field)
                                .gte(value.doubleValue())
                        )
                )
        );
    }

}
