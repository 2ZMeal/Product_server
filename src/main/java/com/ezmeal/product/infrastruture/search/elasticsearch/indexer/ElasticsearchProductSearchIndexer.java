package com.ezmeal.product.infrastruture.search.elasticsearch.indexer;

import com.ezmeal.product.application.search.ProductSearchIndexCommand;
import com.ezmeal.product.application.search.ProductSearchIndexer;
import com.ezmeal.product.infrastruture.search.elasticsearch.document.ProductSearchDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticsearchProductSearchIndexer implements ProductSearchIndexer {

    private final ProductSearchDocumentRepository productSearchDocumentRepository;
    @Override
    public void save(ProductSearchIndexCommand command) {
        ProductSearchDocument document = ProductSearchDocument.from(command);
        productSearchDocumentRepository.save(document);
    }

    @Override
    public void delete(String productId) {
        productSearchDocumentRepository.deleteById(productId);
    }
}
