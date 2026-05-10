package com.ezmeal.product.infrastruture.search.elasticsearch.log;

import com.ezmeal.product.infrastruture.search.elasticsearch.document.ProductSearchLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchLogDocumentRepository extends ElasticsearchRepository<ProductSearchLogDocument, String> {

}
