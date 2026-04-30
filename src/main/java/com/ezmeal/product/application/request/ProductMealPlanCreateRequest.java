package com.ezmeal.product.application.request;

import java.time.DayOfWeek;

public record ProductMealPlanCreateRequest(
        DayOfWeek dayOfWeek,
        String menuName,
        String allergyInfo,
        String nutritionInfo
) {
}
