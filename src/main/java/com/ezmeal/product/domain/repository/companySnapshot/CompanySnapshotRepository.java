package com.ezmeal.product.domain.repository.companySnapshot;

import com.ezmeal.product.domain.model.companySnapShot.CompanySnapshot;
import java.util.Optional;
import java.util.UUID;

public interface CompanySnapshotRepository {

    CompanySnapshot save(CompanySnapshot companySnapshot);

    Optional<CompanySnapshot> findByCompanyId(UUID companyId);

    Optional<CompanySnapshot> findByCompanyIdAndDeletedAtIsNull(UUID companyId);

}
