package com.ezmeal.product.infrastruture.message.kafka.consumer;

import com.ezmeal.product.application.message.CompanyDeletedMessage;
import com.ezmeal.product.application.message.CompanySnapshotUpdatedMessage;
import com.ezmeal.product.application.service.CompanySnapshotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyEventConsumer {
    private final CompanySnapshotService companySnapshotService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "company.snapshot.updated", groupId = "product-group")
    public void handleCompanySnapshotUpdated(String payload) {
        try {
            CompanySnapshotUpdatedMessage message =
                    objectMapper.readValue(payload, CompanySnapshotUpdatedMessage.class);

            log.info("[Kafka] company.snapshot.updated 수신. companyId={}, eventId={}",
                    message.companyId(),
                    message.eventId());

            companySnapshotService.upsert(message);
        } catch (JsonProcessingException e) {
            log.error("[Kafka] company.snapshot.updated 메시지 파싱 실패. payload={}", payload, e);
        }
    }

    @KafkaListener(topics = "company.deleted", groupId = "product-group")
    public void handleCompanyDeleted(String payload) {
        try {
            CompanyDeletedMessage message =
                    objectMapper.readValue(payload, CompanyDeletedMessage.class);

            log.info("[Kafka] company.deleted 수신. companyId={}, eventId={}",
                    message.companyId(),
                    message.eventId());

            companySnapshotService.delete(message.companyId(), "KAFKA");
        } catch (JsonProcessingException e) {
            log.error("[Kafka] company.deleted 메시지 파싱 실패. payload={}", payload, e);
        }
    }
}
