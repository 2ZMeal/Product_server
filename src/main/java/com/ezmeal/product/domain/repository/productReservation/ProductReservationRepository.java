package com.ezmeal.product.domain.repository.productReservation;

import com.ezmeal.product.domain.model.productReservation.ProductReservation;
import java.util.Optional;
import java.util.UUID;

public interface ProductReservationRepository {

    ProductReservation save(ProductReservation productReservation);
    Optional<ProductReservation> findByOrderIdAndProductId(UUID orderId, UUID productId);
    boolean existsByOrderIdAndProductId(UUID orderId, UUID productId);
}
