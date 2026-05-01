package com.ezmeal.product.domain.repository.product;

import com.ezmeal.product.domain.model.product.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);
    Optional<Product> findByIdAndDeletedAtIsNull(UUID productId);
}
