package com.ezmeal.product.application.event;

import com.ezmeal.product.domain.event.payload.ProductCreatedEvent;
import com.ezmeal.product.domain.event.payload.ProductDeletedEvent;
import com.ezmeal.product.domain.event.payload.ProductReservationRestoreFailedEvent;
import com.ezmeal.product.domain.event.payload.ProductReservationRestoredEvent;
import com.ezmeal.product.domain.event.payload.ProductUpdatedEvent;
import com.ezmeal.product.domain.event.producer.ProductEventProducer;
import com.ezmeal.product.domain.event.producer.ProductReservationEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProductKafkaListener {

    private final ProductEventProducer productEventProducer;
    private final ProductReservationEventProducer productReservationEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        productEventProducer.publishCreatedEvent(event);
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductUpdatedEvent(ProductUpdatedEvent event) {
        productEventProducer.publishUpdatedEvent(event);
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductDeletedEvent(ProductDeletedEvent event) {
        productEventProducer.publishDeletedEvent(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductReservationRestoredEvent(ProductReservationRestoredEvent event) {
        productReservationEventProducer.publishRestoredEvent(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductReservationRestoreEventFailed(ProductReservationRestoreFailedEvent event) {
        productReservationEventProducer.publishRestoreFailedEvent(event);
    }

}
