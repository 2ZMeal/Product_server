package com.ezmeal.product.application.response;

import com.ezmeal.product.domain.model.product.Product;
import java.util.UUID;

public record ProductInfo(
        UUID productId,
        UUID companyId,
        String name,
        Integer price
) {
    public static ProductInfo from(Product product) {
        return new ProductInfo(
                product.getId(),
                product.getCompanyId(),
                product.getName(),
                product.getPrice()
        );
    }
}
