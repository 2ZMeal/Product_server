package com.ezmeal.product.infrastruture.persistence.companySnapshotRepository;

import com.ezmeal.product.domain.model.companySnapShot.CompanySnapshot;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCompanySnapshotRepository extends JpaRepository<CompanySnapshot, UUID> {

    Optional<CompanySnapshot> findByCompanyId(UUID companyId);

    Optional<CompanySnapshot> findByCompanyIdAndDeletedAtIsNull(UUID companyId);
}
