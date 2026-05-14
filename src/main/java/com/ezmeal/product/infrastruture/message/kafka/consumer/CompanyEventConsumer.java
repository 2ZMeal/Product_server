package com.ezmeal.product.infrastruture.message.kafka.consumer;

import com.ezmeal.common.message.EventEnvelope;
import com.ezmeal.common.message.inbox.InboxProcessor;
import com.ezmeal.product.application.message.CompanyDeletedMessage;
import com.ezmeal.product.application.message.CompanySnapshotUpdatedMessage;
import com.ezmeal.product.application.service.CompanySnapshotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyEventConsumer {
    private final CompanySnapshotService companySnapshotService;
    private final InboxProcessor inboxProcessor;

    @KafkaListener(topics = "company.snapshot.updated", groupId = "product-group")
    public void handleCompanySnapshotUpdated(EventEnvelope<CompanySnapshotUpdatedMessage> envelope) {
        inboxProcessor.processOnce(envelope.eventId(), () -> {
            CompanySnapshotUpdatedMessage payload = envelope.payload();
            companySnapshotService.upsert(payload);

        });
    }

    //원본데이터가 아니므로 KAFKA로 삭제되었다는 문구만 남김.
    @KafkaListener(topics = "company.deleted", groupId = "product-group")
    public void handleCompanyDeleted(EventEnvelope<CompanyDeletedMessage> envelope) {
        inboxProcessor.processOnce(envelope.eventId(), () -> {
            CompanyDeletedMessage payload = envelope.payload();

            companySnapshotService.delete(payload.companyId(), "KAFKA");
        });
    }
}
