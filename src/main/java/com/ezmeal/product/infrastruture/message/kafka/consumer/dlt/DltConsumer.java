package com.ezmeal.product.infrastruture.message.kafka.consumer.dlt;

import com.ezmeal.common.message.EventEnvelope;
import com.ezmeal.common.message.inbox.InboxProcessor;
import com.ezmeal.product.application.message.OrderCancelledMessage;
import com.ezmeal.product.domain.event.payload.ProductReservationRestoreFailedEvent;
import com.ezmeal.product.domain.event.producer.ProductReservationEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DltConsumer {

    private final InboxProcessor inboxProcessor;
    private final ProductReservationEventProducer productReservationEventProducer;

    @KafkaListener(
            topics = "order.cancelled.DLT",
            groupId = "product-dlt-group",
            containerFactory = "kafkaDltListenerContainerFactory"
    )
    public void handleOrderCancelledDlt(EventEnvelope<OrderCancelledMessage> envelope) {
        try {
            inboxProcessor.processOnce(envelope.eventId() + ":DLT", () -> {
                OrderCancelledMessage payload = envelope.payload();

                ProductReservationRestoreFailedEvent failedEvent =
                        ProductReservationRestoreFailedEvent.of(
                                payload.orderId(),
                                payload.productId(),
                                "재고 복구 재시도 최종 실패"
                        );

                productReservationEventProducer.publishRestoreFailedEvent(failedEvent);
            });
        } catch (Exception e) {
            log.error(
                    "order.cancelled.DLT 처리 실패. eventId: {}, aggregateId: {}",
                    envelope.eventId(),
                    envelope.aggregateId(),
                    e
            );
        }
    }
}
