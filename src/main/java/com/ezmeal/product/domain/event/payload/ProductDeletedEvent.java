package com.ezmeal.product.domain.event.payload;

import com.ezmeal.product.domain.event.ProductEventType;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDeletedEvent {

    private UUID eventId;
    private ProductEventType productEventType;
    private OffsetDateTime occurredAt;
    private UUID productId;
    private UUID companyId;

    public static ProductDeletedEvent of(UUID productId, UUID companyId
    ) {
        return new ProductDeletedEvent(UUID.randomUUID(),
                ProductEventType.PRODUCT_DELETED,
                OffsetDateTime.now(), productId, companyId);
    }
}
