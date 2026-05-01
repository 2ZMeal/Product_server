package com.ezmeal.product.application.response;

import com.ezmeal.product.domain.model.product.ProductMealPlan;
import java.time.DayOfWeek;
import java.util.UUID;

public record ProductMealPlanResponse (
        UUID mealPlanId,
        DayOfWeek dayOfWeek,
        String menuName,
        String allergyInfo,
        String nutritionInfo
){
    public static ProductMealPlanResponse from(ProductMealPlan mealPlan) {
        return new ProductMealPlanResponse(
                mealPlan.getId(),
                mealPlan.getDayOfWeek(),
                mealPlan.getMenuName(),
                mealPlan.getAllergyInfo(),
                mealPlan.getNutritionInfo()
        );
    }
}
