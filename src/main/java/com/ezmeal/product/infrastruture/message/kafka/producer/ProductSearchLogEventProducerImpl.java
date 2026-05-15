package com.ezmeal.product.infrastruture.message.kafka.producer;

import com.ezmeal.common.message.EventEnvelope;
import com.ezmeal.product.domain.event.payload.ProductEventType;
import com.ezmeal.product.domain.event.payload.ProductSearchLoggedEvent;
import com.ezmeal.product.domain.event.producer.ProductSearchLogEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductSearchLogEventProducerImpl implements ProductSearchLogEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publishSearchLoggedEvent(ProductSearchLoggedEvent event) {
        try {
            EventEnvelope<ProductSearchLoggedEvent> envelope =
                    EventEnvelope.of(
                            ProductEventType.PRODUCT_SEARCH_LOGGED.name(),
                            event.getUserId(),
                            event
                    );

            String payload = objectMapper.writeValueAsString(envelope);

            kafkaTemplate.send("product.search.logged", event.getUserId(), payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.warn("검색 로그 이벤트 발행 실패.", ex);
                        }
                    });
        } catch (JsonProcessingException e) {
            log.warn("검색 로그 이벤트 직렬화 실패.", e);
        }
    }
}
