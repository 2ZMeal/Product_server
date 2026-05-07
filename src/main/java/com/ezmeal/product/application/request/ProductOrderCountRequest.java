package com.ezmeal.product.application.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ProductOrderCountRequest(
        @NotNull UUID orderId,
        @NotNull Integer quantity
) {
}
