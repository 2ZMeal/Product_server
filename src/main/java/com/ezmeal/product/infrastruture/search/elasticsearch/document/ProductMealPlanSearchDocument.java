package com.ezmeal.product.infrastruture.search.elasticsearch.document;

import com.ezmeal.product.application.search.ProductMealPlanSearchCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProductMealPlanSearchDocument {
    private String dayOfWeek;
    private String menuName;
    private String allergyInfo;
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
