package com.ezmeal.product.infrastruture.search.elasticsearch.indexer;

import com.ezmeal.product.infrastruture.search.elasticsearch.document.ProductSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchDocumentRepository extends ElasticsearchRepository<ProductSearchDocument, String> {


}
