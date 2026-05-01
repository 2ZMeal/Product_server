package com.ezmeal.product.application.request;

import com.ezmeal.product.domain.model.product.ProductCategory;
import com.ezmeal.product.domain.model.product.ProductMealPeriod;
import java.util.List;
import java.util.UUID;

public record ProductUpdateRequest(
        UUID companyId,
        String name,
        String description,
        Integer price,
        Integer maxOrderCount,
        ProductCategory category,
        ProductMealPeriod mealPeriod,
        List<ProductMealPlanUpdateRequest> mealPlans
) {
}
