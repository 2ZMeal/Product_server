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
public class ProductDeletedEvent implements DomainEvent {

    private UUID productId;
    private UUID companyId;

    public static ProductDeletedEvent of(UUID productId, UUID companyId
    ) {
        return new ProductDeletedEvent(productId, companyId);
    }
}
