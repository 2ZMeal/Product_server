package com.ezmeal.product.application.search.recommend;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRecommendationReader {

    List<ProductRecommendationKeywordResult> findTopKeywords(
            String userId,
            LocalDateTime from,
            int limit
    );
}
