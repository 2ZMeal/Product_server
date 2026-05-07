package com.ezmeal.product.domain.event.producer;

import com.ezmeal.product.domain.event.payload.ProductReservationRestoreFailedEvent;
import com.ezmeal.product.domain.event.payload.ProductReservationRestoredEvent;

public interface ProductReservationEventProducer {
    void publishRestoredEvent(ProductReservationRestoredEvent event);

    void publishRestoreFailedEvent(ProductReservationRestoreFailedEvent event);
}
