package com.ezmeal.product.application.search;

import com.ezmeal.product.application.request.ProductSearchRequest;
import org.springframework.data.domain.Page;

public interface ProductSearchReader {

    Page<ProductSearchResult> search(ProductSearchRequest request);
}
