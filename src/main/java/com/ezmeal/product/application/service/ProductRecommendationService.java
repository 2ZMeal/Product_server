package com.ezmeal.product.application.service;

import com.ezmeal.product.application.search.ProductSearchResult;
import com.ezmeal.product.application.search.recommend.ProductRecommendationKeywordResult;
import com.ezmeal.product.application.search.recommend.ProductRecommendationProductReader;
import com.ezmeal.product.application.search.recommend.ProductRecommendationReader;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductRecommendationService {
    private static final int DEFAULT_KEYWORD_LIMIT = 5;
    private static final int DEFAULT_LOOKBACK_DAYS = 30;
    private static final int DEFAULT_RECOMMENDATION_LIMIT = 10;

    private final ProductRecommendationReader productRecommendationReader;
    private final ProductRecommendationProductReader productRecommendationProductReader;

    public List<ProductSearchResult> recommendProducts(String userId) {
        if (userId == null || userId.isBlank()) {
            return List.of();
        }

        LocalDateTime from = LocalDateTime.now().minusDays(DEFAULT_LOOKBACK_DAYS);

        List<ProductRecommendationKeywordResult> topKeywords =
                productRecommendationReader.findTopKeywords(
                        userId,
                        from,
                        DEFAULT_KEYWORD_LIMIT
                );

        if (topKeywords.isEmpty()) {
            return List.of();
        }

        return productRecommendationProductReader.recommendByKeywords(
                topKeywords,
                DEFAULT_RECOMMENDATION_LIMIT
        );
    }
}
