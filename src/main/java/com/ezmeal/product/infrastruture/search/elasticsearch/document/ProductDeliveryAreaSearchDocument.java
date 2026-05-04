package com.ezmeal.product.infrastruture.search.elasticsearch.document;

import com.ezmeal.product.application.search.ProductDeliveryAreaSearchCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProductDeliveryAreaSearchDocument {
    private String region;
    private String mealPeriod;
    private String estimatedArrivalStartTime;
    private String estimatedArrivalEndTime;

    public static ProductDeliveryAreaSearchDocument from(ProductDeliveryAreaSearchCommand command) {
        return ProductDeliveryAreaSearchDocument.builder()
                .region(command.region())
                .mealPeriod(command.mealPeriod())
                .estimatedArrivalStartTime(command.estimatedArrivalStartTime())
                .estimatedArrivalEndTime(command.estimatedArrivalEndTime())
                .build();
    }
}
