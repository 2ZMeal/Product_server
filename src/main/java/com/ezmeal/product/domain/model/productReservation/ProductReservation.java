package com.ezmeal.product.domain.model.productReservation;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_reservation", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_product_reservation_order_product",
                columnNames = {"order_id", "product_id"}
        )
})
public class ProductReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductReservationStatus status;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    public ProductReservation(UUID orderId, UUID productId, Integer quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = ProductReservationStatus.RESERVED;
    }

    public void restore() {
        this.status = ProductReservationStatus.RESTORED;
        this.restoredAt = LocalDateTime.now();
    }

    public boolean isRestored() {
        return this.status == ProductReservationStatus.RESTORED;
    }

    public boolean isReserved() {
        return this.status == ProductReservationStatus.RESERVED;
    }
}
