package com.ezmeal.product.application.search;

public record ProductMealPlanSearchCommand(
        String dayOfWeek,
        String menuName,
        String allergyInfo,
        String nutritionInfo
) {
}
