package com.ezmeal.product.infrastructure.message.kafka.consumer;

import com.ezmeal.common.message.EventEnvelope;
import com.ezmeal.common.message.inbox.InboxProcessor;
import com.ezmeal.product.application.message.OrderCancelledMessage;
import com.ezmeal.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final ProductService productService;
    private final InboxProcessor inboxProcessor;

    @KafkaListener(topics = "order.cancelled", groupId = "product-group")
    public void handleOrderCancelled(EventEnvelope<OrderCancelledMessage> envelope) {
        inboxProcessor.processOnce(envelope.eventId(), () -> {
            OrderCancelledMessage payload = envelope.payload();
            productService.restoreReservedQuantity(payload.productId(), payload.orderId());
        });
    }
}
