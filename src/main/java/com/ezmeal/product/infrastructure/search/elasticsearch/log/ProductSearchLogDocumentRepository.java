package com.ezmeal.product.infrastructure.search.elasticsearch.log;

import com.ezmeal.product.infrastructure.search.elasticsearch.document.ProductSearchLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchLogDocumentRepository extends ElasticsearchRepository<ProductSearchLogDocument, String> {

}
