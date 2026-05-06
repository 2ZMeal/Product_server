package com.ezmeal.product.infrastruture.search.elasticsearch.document;

import com.ezmeal.product.application.search.ProductDeliveryAreaSearchCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProductDeliveryAreaSearchDocument {
    @Field(type = FieldType.Keyword)
    private String region;
    @Field(type = FieldType.Keyword)
    private String mealPeriod;
    @Field(type = FieldType.Keyword)
    private String estimatedArrivalStartTime;
    @Field(type = FieldType.Keyword)
    private String estimatedArrivalEndTime;

    @Field(type = FieldType.Integer)
    private Integer estimatedArrivalStartMinute;
    @Field(type = FieldType.Integer)
    private Integer estimatedArrivalEndMinute;


    public static ProductDeliveryAreaSearchDocument from(ProductDeliveryAreaSearchCommand command) {
        return ProductDeliveryAreaSearchDocument.builder()
                .region(command.region())
                .mealPeriod(command.mealPeriod())
                .estimatedArrivalStartTime(command.estimatedArrivalStartTime())
                .estimatedArrivalEndTime(command.estimatedArrivalEndTime())
                .estimatedArrivalStartMinute(command.estimatedArrivalStartMinute())
                .estimatedArrivalEndMinute(command.estimatedArrivalEndMinute())
                .build();
    }
}
