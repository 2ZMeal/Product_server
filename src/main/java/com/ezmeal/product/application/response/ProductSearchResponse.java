package com.ezmeal.product.application.response;

import com.ezmeal.product.application.search.ProductSearchResult;
import java.time.LocalDateTime;
import java.util.List;

public record ProductSearchResponse(
        String productId,
        String companyId,
        String companyName,
        String name,
        String description,
        Integer price,
        Integer maxOrderCount,
        Boolean available,
        String category,
        String mealPeriod,
        List<String> availableDays,
        List<String> deliveryRegions,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ProductSearchResponse from(ProductSearchResult result) {
        return new ProductSearchResponse(
                result.productId(),
                result.companyId(),
                result.companyName(),
                result.name(),
                result.description(),
                result.price(),
                result.maxOrderCount(),
                result.available(),
                result.category(),
                result.mealPeriod(),
                result.availableDays(),
                result.deliveryRegions(),
                result.createdAt(),
                result.modifiedAt()
        );
    }
}
