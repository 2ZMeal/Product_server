package com.ezmeal.product.application.response;

import java.time.DayOfWeek;

public record ProductMealPlanResponse(
        DayOfWeek dayOfWeek,
        String menuName,
        String allergyInfo,
        String nutritionInfo,
        String imageUrl
) {
}
