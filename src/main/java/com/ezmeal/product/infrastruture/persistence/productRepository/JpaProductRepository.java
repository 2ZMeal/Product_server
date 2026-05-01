package com.ezmeal.product.infrastruture.persistence.productRepository;

import com.ezmeal.product.domain.model.product.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByIdAndDeletedAtIsNull(UUID productId);
}
