package com.ezmeal.product.infrastruture.persistence.productReservationRepository;

import com.ezmeal.product.domain.model.productReservation.ProductReservation;
import com.ezmeal.product.domain.repository.productReservation.ProductReservationRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductReservationRepositoryImpl implements ProductReservationRepository {

    private final JpaProductReservationRepository jpaProductReservationRepository;

    @Override
    public ProductReservation save(ProductReservation productReservation) {
        return jpaProductReservationRepository.save(productReservation);
    }

    @Override
    public Optional<ProductReservation> findByOrderIdAndProductId(UUID orderId, UUID productId) {
        return jpaProductReservationRepository.findByOrderIdAndProductId(orderId, productId);
    }

    @Override
    public boolean existsByOrderIdAndProductId(UUID orderId, UUID productId) {
        return jpaProductReservationRepository.existsByOrderIdAndProductId(orderId, productId);
    }

    @Override
    public Optional<ProductReservation> findByOrderIdAndProductIdForUpdate(UUID orderId, UUID productId) {
        return jpaProductReservationRepository.findByOrderIdAndProductIdForUpdate(orderId, productId);
    }
}
