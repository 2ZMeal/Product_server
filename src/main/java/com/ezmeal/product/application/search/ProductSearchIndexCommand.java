package com.ezmeal.product.application.search;

import java.time.LocalDateTime;
import java.util.List;

public record ProductSearchIndexCommand(
        String productId,

        String companyId,
        String companyName,
        String companyDescription,

        String name,
        String description,

        Integer price,
        Integer maxOrderCount,
        Boolean available,

        String category,
        String mealPeriod,

        List<String> availableDays,
        List<String> menuNames,
        List<String> deliveryRegions,

        List<ProductMealPlanSearchCommand> mealPlans,
        List<ProductDeliveryAreaSearchCommand> deliveryAreas,

        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

}
