package com.ezmeal.product.application.message;

import com.ezmeal.common.message.DomainEvent;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CompanyDeletedMessage(
        UUID companyId
) implements DomainEvent {
}
