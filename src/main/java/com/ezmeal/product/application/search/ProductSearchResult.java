package com.ezmeal.product.application.search;

import java.time.LocalDateTime;
import java.util.List;

public record ProductSearchResult(
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
}
