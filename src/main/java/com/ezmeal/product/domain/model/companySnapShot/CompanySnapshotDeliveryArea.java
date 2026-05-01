package com.ezmeal.product.domain.model.companySnapShot;

import com.ezmeal.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_company_snapshot_delivery_area", schema = "product_service")
public class CompanySnapshotDeliveryArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_snapshot_delivery_area_id")
    private UUID id;

    @Column(name = "delivery_area_id", nullable = false)
    private UUID deliveryAreaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_snapshot_id", nullable = false)
    private CompanySnapshot companySnapshot;

    @Column(name = "delivery_region", nullable = false)
    private String region;

    @Column(name = "meal_period", nullable = false)
    private String mealPeriod;

    @Column(name = "estimated_arrival_start_time", nullable = false)
    private LocalTime estimatedArrivalStartTime;

    @Column(name = "estimated_arrival_end_time", nullable = false)
    private LocalTime estimatedArrivalEndTime;

    public CompanySnapshotDeliveryArea(UUID deliveryAreaId, String region, String mealPeriod,
                                       LocalTime estimatedArrivalStartTime,
                                       LocalTime estimatedArrivalEndTime) {
        this.deliveryAreaId = deliveryAreaId;
        this.region = region;
        this.mealPeriod = mealPeriod;
        this.estimatedArrivalStartTime = estimatedArrivalStartTime;
        this.estimatedArrivalEndTime = estimatedArrivalEndTime;
    }

    void assignCompanySnapshot(CompanySnapshot companySnapshot) {
        if (companySnapshot == null) {
            throw new IllegalArgumentException("업체 스냅샷은 필수입니다.");
        }
        this.companySnapshot = companySnapshot;
    }
}
