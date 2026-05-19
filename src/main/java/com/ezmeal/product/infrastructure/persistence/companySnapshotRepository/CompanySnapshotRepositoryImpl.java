package com.ezmeal.product.infrastructure.persistence.companySnapshotRepository;

import com.ezmeal.product.domain.model.companySnapShot.CompanySnapshot;
import com.ezmeal.product.domain.repository.companySnapshot.CompanySnapshotRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanySnapshotRepositoryImpl implements CompanySnapshotRepository {

    private final JpaCompanySnapshotRepository jpaCompanySnapshotRepository;


    @Override
    public CompanySnapshot save(CompanySnapshot companySnapshot) {
        return jpaCompanySnapshotRepository.save(companySnapshot);
    }

    @Override
    public Optional<CompanySnapshot> findByCompanyId(UUID companyId) {
        return jpaCompanySnapshotRepository.findByCompanyId(companyId);
    }

    @Override
    public Optional<CompanySnapshot> findByCompanyIdAndDeletedAtIsNull(UUID companyId) {
        return jpaCompanySnapshotRepository.findByCompanyIdAndDeletedAtIsNull(companyId);
    }

    @Override
    public Optional<CompanySnapshot> findWithDeliveryAreasByCompanyIdAndDeletedAtIsNull(UUID companyId) {
        return jpaCompanySnapshotRepository.findWithDeliveryAreasByCompanyIdAndDeletedAtIsNull(companyId);
    }
}
