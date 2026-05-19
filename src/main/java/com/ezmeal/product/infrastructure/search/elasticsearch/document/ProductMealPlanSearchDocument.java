package com.ezmeal.product.infrastructure.search.elasticsearch.document;

import com.ezmeal.product.application.search.ProductMealPlanSearchCommand;
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
public class ProductMealPlanSearchDocument {
    @Field(type = FieldType.Keyword)
    private String dayOfWeek;
    @Field(type = FieldType.Text)
    private String menuName;
    @Field(type = FieldType.Text)
    private String allergyInfo;
    @Field(type = FieldType.Text)
    private String nutritionInfo;

    public static ProductMealPlanSearchDocument from(ProductMealPlanSearchCommand command) {
        return ProductMealPlanSearchDocument.builder()
                .dayOfWeek(command.dayOfWeek())
                .menuName(command.menuName())
                .allergyInfo(command.allergyInfo())
                .nutritionInfo(command.nutritionInfo())
                .build();
    }
}
