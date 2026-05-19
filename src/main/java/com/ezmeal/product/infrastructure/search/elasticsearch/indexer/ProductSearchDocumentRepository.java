package com.ezmeal.product.infrastructure.search.elasticsearch.indexer;

import com.ezmeal.product.infrastructure.search.elasticsearch.document.ProductSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchDocumentRepository extends ElasticsearchRepository<ProductSearchDocument, String> {


}
