package com.ezmeal.product.infrastructure.persistence.companySnapshotRepository;

import com.ezmeal.product.domain.model.companySnapShot.CompanySnapshot;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCompanySnapshotRepository extends JpaRepository<CompanySnapshot, UUID> {

    Optional<CompanySnapshot> findByCompanyId(UUID companyId);

    Optional<CompanySnapshot> findByCompanyIdAndDeletedAtIsNull(UUID companyId);

    @EntityGraph(attributePaths = "deliveryAreas")
    Optional<CompanySnapshot> findWithDeliveryAreasByCompanyIdAndDeletedAtIsNull(UUID companyId);

}
