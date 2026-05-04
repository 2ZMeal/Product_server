package com.ezmeal.product.application.search;

public interface ProductSearchIndexer {
    void save(ProductSearchIndexCommand command);

    void delete(String productId);
}
