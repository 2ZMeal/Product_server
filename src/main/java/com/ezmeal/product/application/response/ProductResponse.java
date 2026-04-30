package com.ezmeal.product.application.response;

import com.ezmeal.product.domain.model.Product;
import com.ezmeal.product.domain.model.ProductCategory;
import com.ezmeal.product.domain.model.ProductMealPeriod;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID productId,
        UUID companyId,
        String name,
        String description,
        Integer price,
        Integer maxOrderCount,
        ProductCategory category,
        ProductMealPeriod mealPeriod,
        List<ProductMealPlanResponse> mealPlans
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCompanyId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMaxOrderCount(),
                product.getCategory(),
                product.getMealPeriod(),
                product.getMealPlans().stream()
                        .map(ProductMealPlanResponse::from)
                        .toList()
        );
    }
}
