package com.ezmeal.product.infrastruture.persistence.productRepository;

import com.ezmeal.product.domain.model.product.Product;
import com.ezmeal.product.domain.repository.product.ProductRepository;
import java.util.List;
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

    @Override
    public Optional<Product> findByIdAndDeletedAtIsNullForUpdate(UUID productId) {
        return jpaProductRepository.findByIdAndDeletedAtIsNullForUpdate(productId);
    }

    @Override
    public List<Product> findAllByIdInAndDeletedAtIsNull(List<UUID> productIds) {
        return jpaProductRepository.findAllByIdInAndDeletedAtIsNull(productIds);
    }
}
