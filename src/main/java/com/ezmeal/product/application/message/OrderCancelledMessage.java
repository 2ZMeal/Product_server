package com.ezmeal.product.application.message;

import com.ezmeal.common.message.DomainEvent;
import java.util.UUID;

public record OrderCancelledMessage(
        UUID productId,
        UUID orderId,
        Integer quantity
) implements DomainEvent {
}
