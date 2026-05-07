package com.ezmeal.product.infrastruture.message.kafka.consumer;

import com.ezmeal.product.application.message.OrderCancelledMessage;
import com.ezmeal.product.application.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order.cancelled", groupId = "product-group")
    public void handleOrderCancelled(String payload) {
        try {
            OrderCancelledMessage message =
                    objectMapper.readValue(payload, OrderCancelledMessage.class);

            log.info("[Kafka] order.cancelled 수신. productId={}, orderId={},quantity={}",
                    message.productId(),
                    message.orderId(),
                    message.quantity());

            productService.restoreReservedQuantity(message.productId(),message.orderId());
        } catch (JsonProcessingException e) {
            log.error("[Kafka] order.cancelled 메시지 파싱 실패. payload={}", payload, e);
        }
    }
}
