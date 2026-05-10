package com.ezmeal.product.application.search.recommend;

import com.ezmeal.product.application.search.ProductSearchResult;
import java.util.List;

public interface ProductRecommendationProductReader {
    List<ProductSearchResult> recommendByKeywords(
            List<ProductRecommendationKeywordResult> keywords,
            int limit
    );
}
