package com.ezmeal.product.infrastruture.search.elasticsearch.reader;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.ezmeal.product.application.request.ProductSearchRequest;
import com.ezmeal.product.application.search.ProductSearchReader;
import com.ezmeal.product.application.search.ProductSearchResult;
import com.ezmeal.product.infrastruture.search.elasticsearch.document.ProductSearchDocument;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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


@Component
@RequiredArgsConstructor
public class ElasticsearchProductSearchReader implements ProductSearchReader {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<ProductSearchResult> search(ProductSearchRequest request) {
        int page = request.page() == null ? 0 : request.page();
        int size = request.size() == null ? 10 : request.size();
        String sortBy = request.sortBy() == null ? "createdAt" : request.sortBy();
        String direction = request.direction() == null ? "DESC" : request.direction();

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(direction), sortBy)
        );

        NativeQuery query = buildQuery(request, pageRequest);

        SearchHits<ProductSearchDocument> searchHits =
                elasticsearchOperations.search(query, ProductSearchDocument.class);

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

        if (request.region() != null && !request.region().isBlank()) {
            filters.add(termQuery("deliveryRegions", request.region()));
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
}
