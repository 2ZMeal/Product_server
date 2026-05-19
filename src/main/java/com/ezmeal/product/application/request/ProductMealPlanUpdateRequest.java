package com.ezmeal.product.application.request;

import java.time.DayOfWeek;

public record ProductMealPlanUpdateRequest(
        DayOfWeek dayOfWeek,
        String menuName,
        String allergyInfo,
        String nutritionInfo
) {
}
