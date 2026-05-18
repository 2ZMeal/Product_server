package com.ezmeal.product.application.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record ProductOrderQuantityBulkReserveRequest(
        @NotNull UUID orderId,
        @NotEmpty List<ProductReserveItem> items
) {
    public record ProductReserveItem(
            @NotNull UUID productId,
            @NotNull Integer quantity
    ) {
    }
}
