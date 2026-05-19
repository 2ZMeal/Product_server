package com.ezmeal.product.infrastructure.message.kafka.producer;

import com.ezmeal.common.message.CommonKafkaEventPublisher;
import com.ezmeal.product.domain.event.payload.ProductEventType;
import com.ezmeal.product.domain.event.payload.ProductReservationRestoreFailedEvent;
import com.ezmeal.product.domain.event.payload.ProductReservationRestoredEvent;
import com.ezmeal.product.domain.event.producer.ProductReservationEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductReservationEventProducerImpl implements ProductReservationEventProducer {

    private final CommonKafkaEventPublisher commonKafkaEventPublisher;

    @Override
    public void publishRestoredEvent(ProductReservationRestoredEvent event) {
        commonKafkaEventPublisher.publish(
                "product.quantity.restored",
                event.getProductId().toString(),
                ProductEventType.PRODUCT_QUANTITY_RESTORED.name(),
                event
        );
    }

    @Override
    public void publishRestoreFailedEvent(ProductReservationRestoreFailedEvent event) {
        commonKafkaEventPublisher.publish(
                "product.quantity.restore.failed",
                event.getProductId().toString(),
                ProductEventType.PRODUCT_QUANTITY_RESTORE_FAILED.name(),
                event
        );
    }

}
