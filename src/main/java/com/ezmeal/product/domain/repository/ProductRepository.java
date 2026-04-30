package com.ezmeal.product.domain.repository;

import com.ezmeal.product.domain.model.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);
    Optional<Product> findByIdAndDeletedAtIsNull(UUID productId);
}
