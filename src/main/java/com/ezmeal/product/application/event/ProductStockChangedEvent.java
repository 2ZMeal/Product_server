package com.ezmeal.product.application.event;

import java.util.UUID;

public record ProductStockChangedEvent(UUID productId) {
    public static ProductStockChangedEvent of(UUID productId) {
        return new ProductStockChangedEvent(productId);
    }
}
