package com.ezmeal.product.infrastructure.message.kafka.producer;

import com.ezmeal.common.message.CommonKafkaEventPublisher;
import com.ezmeal.product.domain.event.payload.ProductCreatedEvent;
import com.ezmeal.product.domain.event.payload.ProductDeletedEvent;
import com.ezmeal.product.domain.event.payload.ProductEventType;
import com.ezmeal.product.domain.event.payload.ProductUpdatedEvent;
import com.ezmeal.product.domain.event.producer.ProductEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventProducerImpl implements ProductEventProducer {

    private final CommonKafkaEventPublisher commonKafkaEventPublisher;

    @Override
    public void publishCreatedEvent(ProductCreatedEvent event) {
        commonKafkaEventPublisher.publish(
                "product.created",
                event.getProductId().toString(),
                ProductEventType.PRODUCT_CREATED.name(),
                event
        );
    }

    @Override
    public void publishUpdatedEvent(ProductUpdatedEvent event) {
        commonKafkaEventPublisher.publish(
                "product.updated",
                event.getProductId().toString(),
                ProductEventType.PRODUCT_UPDATED.name(),
                event
        );
    }

    @Override
    public void publishDeletedEvent(ProductDeletedEvent event) {
        commonKafkaEventPublisher.publish(
                "product.deleted",
                event.getProductId().toString(),
                ProductEventType.PRODUCT_DELETED.name(),
                event
        );
    }

}
