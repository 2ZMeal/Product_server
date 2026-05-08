package com.ezmeal.product.infrastruture.persistence.productRepository;

import com.ezmeal.product.domain.model.product.Product;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByIdAndDeletedAtIsNull(UUID productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :productId and p.deletedAt is null")
    Optional<Product> findByIdAndDeletedAtIsNullForUpdate(@Param("productId") UUID productId);

    List<Product> findAllByIdInAndDeletedAtIsNull(List<UUID> productIds);
}
