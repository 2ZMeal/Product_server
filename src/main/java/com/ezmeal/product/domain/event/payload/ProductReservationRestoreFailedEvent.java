package com.ezmeal.product.domain.event.payload;

import com.ezmeal.common.message.DomainEvent;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductReservationRestoreFailedEvent implements DomainEvent {
    private UUID orderId;
    private UUID productId;
    private String reason;

    public static ProductReservationRestoreFailedEvent of(UUID orderId, UUID productId, String reason) {
        return new ProductReservationRestoreFailedEvent(
                orderId,
                productId,
                reason
        );
    }
}
