package com.ezmeal.product.application.request;

import com.ezmeal.product.domain.model.product.ProductCategory;
import com.ezmeal.product.domain.model.product.ProductMealPeriod;
import java.util.List;

public record ProductUpdateRequest(
        String name,
        String description,
        Integer price,
        Integer maxOrderCount,
        ProductCategory category,
        ProductMealPeriod mealPeriod,
        List<ProductMealPlanUpdateRequest> mealPlans
) {
}
