package com.ezmeal.product.infrastruture.message.kafka.producer;

import com.ezmeal.product.domain.event.payload.ProductCreatedEvent;
import com.ezmeal.product.domain.event.payload.ProductDeletedEvent;
import com.ezmeal.product.domain.event.payload.ProductUpdatedEvent;
import com.ezmeal.product.domain.event.producer.ProductEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventProducerImpl implements ProductEventProducer {

    private final KafkaEventSender kafkaEventSender;

    @Override
    public void publishCreatedEvent(ProductCreatedEvent event) {
        // 헤더와 페이로드를 함께 담아서 보내는 kafkaEventSender.send를 사용
        kafkaEventSender.send("product.created", event);
    }

    @Override
    public void publishUpdatedEvent(ProductUpdatedEvent event) {
// 헤더와 페이로드를 함께 담아서 보내는 kafkaEventSender.send를 사용
        kafkaEventSender.send("product.updated", event);
    }

    @Override
    public void publishDeletedEvent(ProductDeletedEvent event) {
// 헤더와 페이로드를 함께 담아서 보내는 kafkaEventSender.send를 사용
        kafkaEventSender.send("product.deleted", event);
    }

}
