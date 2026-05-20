package com.ezmeal.product.application.response;

import com.ezmeal.product.domain.model.product.ProductCategory;
import com.ezmeal.product.domain.model.product.ProductMealPeriod;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID productId,
        UUID companyId,
        String name,
        String description,
        Integer price,
        Integer maxOrderCount,
        ProductCategory category,
        ProductMealPeriod mealPeriod,
        String mainImageUrl,
        List<ProductMealPlanResponse> mealPlans
) {
}
