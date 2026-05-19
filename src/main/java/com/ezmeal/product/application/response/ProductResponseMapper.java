package com.ezmeal.product.application.response;

import com.ezmeal.product.application.upload.ProductImageUrlResolver;
import com.ezmeal.product.domain.model.product.Product;
import com.ezmeal.product.domain.model.product.ProductMealPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductResponseMapper {

    private final ProductImageUrlResolver imageUrlResolver;

    public ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCompanyId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMaxOrderCount(),
                product.getCategory(),
                product.getMealPeriod(),
                imageUrlResolver.resolve(product.getMainImageKey()),
                product.getMealPlans().stream()
                        .map(this::toMealPlanResponse)
                        .toList()
        );
    }

    private ProductMealPlanResponse toMealPlanResponse(ProductMealPlan mealPlan) {
        return new ProductMealPlanResponse(
                mealPlan.getDayOfWeek(),
                mealPlan.getMenuName(),
                mealPlan.getAllergyInfo(),
                mealPlan.getNutritionInfo(),
                imageUrlResolver.resolve(mealPlan.getImageKey())
        );
    }
}
