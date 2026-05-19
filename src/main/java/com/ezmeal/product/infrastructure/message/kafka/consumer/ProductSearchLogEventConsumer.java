package com.ezmeal.product.infrastructure.message.kafka.consumer;

import com.ezmeal.common.message.EventEnvelope;
import com.ezmeal.product.application.search.log.ProductSearchLogAppender;
import com.ezmeal.product.application.search.log.ProductSearchLogCommand;
import com.ezmeal.product.domain.event.payload.ProductSearchLoggedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductSearchLogEventConsumer {

    private final ProductSearchLogAppender productSearchLogAppender;

    @KafkaListener(topics = "product.search.logged", groupId = "product-search-log-group")
    public void handleProductSearchLogged(EventEnvelope<ProductSearchLoggedEvent> envelope) {
        ProductSearchLoggedEvent payload = envelope.payload();

        ProductSearchLogCommand command = ProductSearchLogCommand.from(payload);
        productSearchLogAppender.append(command);
    }
}
