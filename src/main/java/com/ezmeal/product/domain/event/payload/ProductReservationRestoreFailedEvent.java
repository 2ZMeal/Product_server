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
public class ProductReservationRestoreFailedEvent {
    private UUID eventId;
    private OffsetDateTime occurredAt;
    private UUID orderId;
    private UUID productId;
    private String reason;

    public static ProductReservationRestoreFailedEvent of(UUID orderId, UUID productId, String reason) {
        return new ProductReservationRestoreFailedEvent(
                UUID.randomUUID(),
                OffsetDateTime.now(),
                orderId,
                productId,
                reason
        );
    }
}
