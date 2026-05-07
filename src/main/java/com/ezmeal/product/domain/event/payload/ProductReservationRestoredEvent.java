package com.ezmeal.product.domain.event.payload;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductReservationRestoredEvent {
    private UUID eventId;
    private OffsetDateTime occurredAt;
    private UUID orderId;
    private UUID productId;
    private Integer quantity;

    public static ProductReservationRestoredEvent of(UUID orderId, UUID productId, Integer quantity) {
        return new ProductReservationRestoredEvent(
                UUID.randomUUID(),
                OffsetDateTime.now(),
                orderId,
                productId,
                quantity
        );
    }
}
