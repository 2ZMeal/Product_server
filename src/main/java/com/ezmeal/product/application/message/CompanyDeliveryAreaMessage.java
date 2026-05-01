package com.ezmeal.product.application.message;

import java.time.LocalTime;
import java.util.UUID;

public record CompanyDeliveryAreaMessage(
        UUID deliveryAreaId,
        String region,
        String mealPeriod,
        LocalTime estimatedArrivalStartTime,
        LocalTime estimatedArrivalEndTime
) {
}
