package com.ezmeal.product.infrastruture.search.elasticsearch.reader;

import com.ezmeal.product.application.search.ProductSearchResult;
import com.ezmeal.product.application.search.recommend.ProductRecommendationKeywordResult;
import com.ezmeal.product.application.search.recommend.ProductRecommendationProductReader;
import com.ezmeal.product.infrastruture.search.elasticsearch.document.ProductSearchDocument;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticSearchProductRecommendationProductReader implements ProductRecommendationProductReader {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<ProductSearchResult> recommendByKeywords(List<ProductRecommendationKeywordResult> keywords, int limit) {

        if (keywords == null || keywords.isEmpty()) {
            return List.of();
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> {
                    b.filter(f -> f.term(t -> t
                            .field("available")
                            .value(true)
                    ));


                    for (ProductRecommendationKeywordResult keyword : keywords) {
                        b.should(s -> s.multiMatch(mm -> mm
                                .query(keyword.keyword())
                                .fields("name^3", "menuNames^2", "description", "companyName")
                                .boost((float) keyword.count())
                        ));
                    }

                    b.minimumShouldMatch("1");

                    return b;
                }))
                .withMaxResults(limit)
                .build();

        SearchHits<ProductSearchDocument> searchHits =
                elasticsearchOperations.search(query, ProductSearchDocument.class);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(this::toResult)
                .toList();
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
