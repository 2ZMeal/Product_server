package com.ezmeal.product.application.message;

import java.util.UUID;

public record OrderCancelledMessage(
        UUID productId,
        UUID orderId,
        Integer quantity
) {
}
