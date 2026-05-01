package com.ezmeal.product.domain.event.payload;

import com.ezmeal.product.domain.model.ProductMealPlan;
import java.time.DayOfWeek;
import java.util.UUID;

public record ProductMealPlanEventPayload(
        UUID mealPlanId,
        DayOfWeek dayOfWeek,
        String menuName,
        String allergyInfo,
        String nutritionInfo
) {
    public static ProductMealPlanEventPayload from(ProductMealPlan mealPlan) {
        return new ProductMealPlanEventPayload(
                mealPlan.getId(),
                mealPlan.getDayOfWeek(),
                mealPlan.getMenuName(),
                mealPlan.getAllergyInfo(),
                mealPlan.getNutritionInfo()
        );
    }
}
