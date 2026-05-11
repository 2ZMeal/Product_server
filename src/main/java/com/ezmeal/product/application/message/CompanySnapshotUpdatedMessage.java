package com.ezmeal.product.application.message;

import com.ezmeal.common.message.DomainEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CompanySnapshotUpdatedMessage(
        UUID companyId,
        UUID managerUserId,
        String companyName,
        String companyLotAddress,
        String companyRoadAddress,
        String companyDescription,

        @JsonProperty
        List<CompanyDeliveryAreaMessage> deliveryAreas
) implements DomainEvent {
}
