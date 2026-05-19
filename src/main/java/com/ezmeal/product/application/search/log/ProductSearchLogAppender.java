package com.ezmeal.product.application.search.log;

public interface ProductSearchLogAppender {

    void append(ProductSearchLogCommand command);
}
