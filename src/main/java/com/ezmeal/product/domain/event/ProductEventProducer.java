package com.ezmeal.product.domain.event;

import com.ezmeal.product.domain.event.payload.ProductCreatedEvent;
import com.ezmeal.product.domain.event.payload.ProductDeletedEvent;
import com.ezmeal.product.domain.event.payload.ProductUpdatedEvent;

public interface ProductEventProducer {
    void publishCreatedEvent(ProductCreatedEvent event);

    void publishUpdatedEvent(ProductUpdatedEvent event);

    void publishDeletedEvent(ProductDeletedEvent event);
}
