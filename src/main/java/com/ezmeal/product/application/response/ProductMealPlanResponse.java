package com.ezmeal.product.application.response;

import com.ezmeal.product.application.upload.ProductImageUrlResolver;
import com.ezmeal.product.domain.model.product.ProductMealPlan;
import java.time.DayOfWeek;

public record ProductMealPlanResponse(
        DayOfWeek dayOfWeek,
        String menuName,
        String allergyInfo,
        String nutritionInfo,
        String imageUrl
) {
}
