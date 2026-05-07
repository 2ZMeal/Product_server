package com.ezmeal.product.infrastruture.message.kafka.producer;

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

    private final KafkaEventSender kafkaEventSender;

    @Override
    public void publishRestoredEvent(ProductReservationRestoredEvent event) {
        kafkaEventSender.send("product.quantity.restored", event);
    }

    @Override
    public void publishRestoreFailedEvent(ProductReservationRestoreFailedEvent event) {
        kafkaEventSender.send("product.quantity.restore.failed", event);
    }

}
