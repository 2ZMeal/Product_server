package com.ezmeal.product.infrastruture.persistence;

import com.ezmeal.product.domain.model.Product;
import com.ezmeal.product.domain.repository.ProductRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Optional<Product> findByIdAndDeletedAtIsNull(UUID productId) {
        return jpaProductRepository.findByIdAndDeletedAtIsNull(productId);
    }
}
