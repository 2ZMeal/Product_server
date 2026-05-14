package com.ezmeal.product.domain.event.producer;

import com.ezmeal.product.domain.event.payload.ProductSearchLoggedEvent;

public interface ProductSearchLogEventProducer {

    void publishSearchLoggedEvent(ProductSearchLoggedEvent event);
}
