package com.ezmeal.product.application.service;

import com.ezmeal.product.application.message.CompanyDeliveryAreaMessage;
import com.ezmeal.product.application.message.CompanySnapshotUpdatedMessage;
import com.ezmeal.product.domain.model.companySnapShot.CompanySnapshot;
import com.ezmeal.product.domain.model.companySnapShot.CompanySnapshotDeliveryArea;
import com.ezmeal.product.domain.repository.companySnapshot.CompanySnapshotRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanySnapshotService {

    private final CompanySnapshotRepository companySnapshotRepository;

    @Transactional
    public void upsert(CompanySnapshotUpdatedMessage message) {

        if (message.deliveryAreas() == null) {
            throw new IllegalArgumentException("배송지역 목록은 null일 수 없습니다.");
        }

        CompanySnapshot companySnapshot = companySnapshotRepository.findByCompanyId(message.companyId())
                .orElseGet(() -> new CompanySnapshot(
                        message.companyId(),
                        message.managerUserId(),
                        message.companyName(),
                        message.companyLotAddress(),
                        message.companyRoadAddress(),
                        message.companyDescription()
                ));

        companySnapshot.update(
                message.managerUserId(),
                message.companyName(),
                message.companyLotAddress(),
                message.companyRoadAddress(),
                message.companyDescription()
        );

        List<CompanySnapshotDeliveryArea> deliveryAreas = message.deliveryAreas().stream()
                .map(this::toDeliveryArea)
                .toList();

        companySnapshot.replaceDeliveryAreas(deliveryAreas);

        companySnapshotRepository.save(companySnapshot);
    }

    @Transactional
    public void delete(UUID companyId, String deletedBy) {
        companySnapshotRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .ifPresent(companySnapshot -> companySnapshot.delete(deletedBy));
    }

    private CompanySnapshotDeliveryArea toDeliveryArea(CompanyDeliveryAreaMessage message) {
        return new CompanySnapshotDeliveryArea(
                message.deliveryAreaId(),
                message.region(),
                message.mealPeriod(),
                message.estimatedArrivalStartTime(),
                message.estimatedArrivalEndTime()
        );
    }
}

