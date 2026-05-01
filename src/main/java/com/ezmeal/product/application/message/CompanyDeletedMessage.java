package com.ezmeal.product.application.message;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CompanyDeletedMessage(
        UUID eventId,
        String companyEventType,
        OffsetDateTime occurredAt,
        UUID companyId
) {
}
