package com.ezmeal.product.application.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CompanyDeliveryAreaMessage(
        UUID deliveryAreaId,
        String region,
        String mealPeriod,
        LocalTime estimatedArrivalStartTime,
        LocalTime estimatedArrivalEndTime
) {
}
