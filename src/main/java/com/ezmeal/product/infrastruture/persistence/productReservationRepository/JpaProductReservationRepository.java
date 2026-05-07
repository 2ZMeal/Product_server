package com.ezmeal.product.infrastruture.persistence.productReservationRepository;

import com.ezmeal.product.domain.model.productReservation.ProductReservation;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface JpaProductReservationRepository extends JpaRepository<ProductReservation, UUID> {

    Optional<ProductReservation> findByOrderIdAndProductId(UUID orderId, UUID productId);
    boolean existsByOrderIdAndProductId(UUID orderId, UUID productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select pr
        from ProductReservation pr
        where pr.orderId = :orderId
          and pr.productId = :productId
        """)
    Optional<ProductReservation> findByOrderIdAndProductIdForUpdate(UUID orderId, UUID productId);
}
