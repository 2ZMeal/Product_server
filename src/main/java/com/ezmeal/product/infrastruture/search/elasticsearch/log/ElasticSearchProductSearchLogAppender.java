package com.ezmeal.product.infrastruture.search.elasticsearch.log;

import com.ezmeal.product.application.search.log.ProductSearchLogAppender;
import com.ezmeal.product.application.search.log.ProductSearchLogCommand;
import com.ezmeal.product.infrastruture.search.elasticsearch.document.ProductSearchLogDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticSearchProductSearchLogAppender implements ProductSearchLogAppender {

    private final ProductSearchLogDocumentRepository productSearchLogDocumentRepository;

    @Override
    public void append(ProductSearchLogCommand command) {
        ProductSearchLogDocument document = ProductSearchLogDocument.from(command);
        productSearchLogDocumentRepository.save(document);
    }
}
