package com.ezmeal.product.infrastruture.persistence.productReservationRepository;

import com.ezmeal.product.domain.model.productReservation.ProductReservation;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductReservationRepository extends JpaRepository<ProductReservation, UUID> {

    Optional<ProductReservation> findByOrderIdAndProductId(UUID orderId, UUID productId);
    boolean existsByOrderIdAndProductId(UUID orderId, UUID productId);
}
