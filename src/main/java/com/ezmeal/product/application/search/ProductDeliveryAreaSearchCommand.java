package com.ezmeal.product.application.search;

public record ProductDeliveryAreaSearchCommand(
        String region,
        String mealPeriod,
        String estimatedArrivalStartTime,
        String estimatedArrivalEndTime
) {
}
