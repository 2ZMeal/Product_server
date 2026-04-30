package com.ezmeal.product.application.request;

import com.ezmeal.product.domain.model.ProductCategory;
import com.ezmeal.product.domain.model.ProductMealPeriod;
import java.util.List;
import java.util.UUID;

public record ProductCreateRequest(
        UUID companyId,
        String name,
        String description,
        Integer price,
        Integer maxOrderCount,
        ProductCategory category,
        ProductMealPeriod mealPeriod,
        List<ProductMealPlanCreateRequest> mealPlans
) {
}
