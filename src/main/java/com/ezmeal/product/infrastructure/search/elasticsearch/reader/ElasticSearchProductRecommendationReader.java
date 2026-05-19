package com.ezmeal.product.infrastructure.search.elasticsearch.reader;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import com.ezmeal.product.application.search.recommend.ProductRecommendationKeywordResult;
import com.ezmeal.product.application.search.recommend.ProductRecommendationReader;
import com.ezmeal.product.infrastructure.search.elasticsearch.document.ProductSearchLogDocument;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ElasticSearchProductRecommendationReader implements ProductRecommendationReader {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<ProductRecommendationKeywordResult> findTopKeywords(String userId, LocalDateTime from, int limit) {

        Aggregation topKeywordsRequestAggregation = Aggregation.of(a -> a
                .terms(t -> t
                        .field("keyword")
                        .size(limit)
                )
        );

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .filter(f -> f.term(t -> t
                                .field("userId")
                                .value(userId)
                        ))
                        .filter(f -> f.range(r -> r.date(d -> d
                                .field("searchedAt")
                                .gte(from.toString())
                        )))
                        .filter(f -> f.exists(e -> e
                                .field("keyword")
                        ))
                ))
                .withMaxResults(0)
                .withAggregation("top_keywords", topKeywordsRequestAggregation)
                .build();

        SearchHits<ProductSearchLogDocument> searchHits =
                elasticsearchOperations.search(query, ProductSearchLogDocument.class);

        ElasticsearchAggregations aggregations =
                (ElasticsearchAggregations) searchHits.getAggregations();

        if (aggregations == null) {
            return List.of();
        }

        ElasticsearchAggregation topKeywordsAggregation =
                aggregations.get("top_keywords");

        if (topKeywordsAggregation == null) {
            return List.of();
        }

        StringTermsAggregate terms = topKeywordsAggregation.aggregation()
                .getAggregate()
                .sterms();

        return terms.buckets().array().stream()
                .map(bucket -> new ProductRecommendationKeywordResult(
                        bucket.key().stringValue(),
                        bucket.docCount()
                ))
                .toList();
    }
}
