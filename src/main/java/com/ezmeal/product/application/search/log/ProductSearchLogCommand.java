package com.ezmeal.product.application.search.log;

import com.ezmeal.product.application.request.ProductSearchRequest;
import java.time.LocalDateTime;

public record ProductSearchLogCommand(
        String userId,
        String keyword,
        String category,
        String mealPeriod,
        String region,
        Integer minPrice,
        Integer maxPrice,
        LocalDateTime searchedAt
) {

    public static ProductSearchLogCommand from(String userId, ProductSearchRequest request) {
        return new ProductSearchLogCommand(
                userId,
                normalize(request.keyword()),
                normalize(request.category()),
                normalize(request.mealPeriod()),
                normalize(request.region()),
                request.minPrice(),
                request.maxPrice(),
                LocalDateTime.now()
        );
    }

    private static String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
