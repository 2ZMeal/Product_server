package com.ezmeal.product.application.message;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


public record CompanySnapshotUpdatedMessage(
        UUID eventId,
        String companyEventType,
        OffsetDateTime occurredAt,
        UUID companyId,
        UUID managerUserId,
        String companyName,
        String companyLotAddress,
        String companyRoadAddress,
        String companyDescription,
        List<CompanyDeliveryAreaMessage> deliveryAreas
) {
}
