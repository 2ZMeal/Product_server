package com.ezmeal.product.domain.model.companySnapShot;

import com.ezmeal.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_company_snapshot", schema = "product_service")
public class CompanySnapshot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_snapshot_id")
    private UUID id;

    @Column(name = "company_id", nullable = false, unique = true)
    private UUID companyId;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "companySnapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanySnapshotDeliveryArea> deliveryAreas = new ArrayList<>();

    @Column(name = "manager_user_id", nullable = false)
    private UUID managerUserId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "lot_address", length = 255)
    private String lotAddress;

    @Column(name = "road_address", length = 255)
    private String roadAddress;

    @Column(name = "description", length = 255)
    private String description;

    public List<CompanySnapshotDeliveryArea> getDeliveryAreas() {
        return Collections.unmodifiableList(deliveryAreas);
    }

    public CompanySnapshot(UUID companyId, UUID managerUserId, String name, String lotAddress,
                           String roadAddress, String description) {

        validate(companyId, managerUserId, name);

        this.companyId = companyId;
        this.managerUserId = managerUserId;
        this.name = name;
        this.lotAddress = lotAddress;
        this.roadAddress = roadAddress;
        this.description = description;
    }

    public void update(UUID managerUserId, String name, String lotAddress, String roadAddress, String description) {

        validate(companyId, managerUserId, name);

        this.managerUserId = managerUserId;
        this.name = name;
        this.lotAddress = lotAddress;
        this.roadAddress = roadAddress;
        this.description = description;
    }

    public void replaceDeliveryAreas(List<CompanySnapshotDeliveryArea> newDeliveryAreas) {
        if (newDeliveryAreas == null) {
            throw new IllegalArgumentException("배송지역 목록은 null일 수 없습니다.");
        }

        List<CompanySnapshotDeliveryArea> replacement = new ArrayList<>();

        for (CompanySnapshotDeliveryArea deliveryArea : newDeliveryAreas) {
            if (deliveryArea == null) {
                throw new IllegalArgumentException("배송지역은 null일 수 없습니다.");
            }

            deliveryArea.assignCompanySnapshot(this);
            replacement.add(deliveryArea);
        }

        this.deliveryAreas.clear();
        this.deliveryAreas.addAll(replacement);
    }

    private void validate(UUID companyId, UUID managerUserId, String name) {
        if (companyId == null) {
            throw new IllegalArgumentException("업체 ID는 필수입니다.");
        }

        if (managerUserId == null) {
            throw new IllegalArgumentException("업체관리자 ID는 필수입니다.");
        }

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("업체명은 필수입니다.");
        }
    }


}
