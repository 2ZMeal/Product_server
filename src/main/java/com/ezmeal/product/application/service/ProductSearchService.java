package com.ezmeal.product.application.service;

import com.ezmeal.product.application.request.ProductSearchRequest;
import com.ezmeal.product.application.response.PageResponse;
import com.ezmeal.product.application.response.ProductSearchResponse;
import com.ezmeal.product.application.search.ProductSearchReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchReader productSearchReader;
    private final ProductSearchLogService productSearchLogService;

    long start = System.currentTimeMillis();

    public PageResponse<ProductSearchResponse> searchProducts(String userId, ProductSearchRequest request) {
        Page<ProductSearchResponse> page = productSearchReader.search(request)
                .map(ProductSearchResponse::from);

        log.info("product search service reader elapsed={}ms", System.currentTimeMillis() - start);

        productSearchLogService.saveIfNeeded(userId, request);

        return PageResponse.from(page);
    }
}
