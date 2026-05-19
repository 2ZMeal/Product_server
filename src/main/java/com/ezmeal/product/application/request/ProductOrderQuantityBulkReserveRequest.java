package com.ezmeal.product.application.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;

public record ProductOrderQuantityBulkReserveRequest(
        @NotNull UUID orderId,
        @NotEmpty List<@Valid ProductReserveItem> items
) {
    public record ProductReserveItem(
            @NotNull UUID productId,
            @NotNull @Positive Integer quantity
    ) {
    }
}
