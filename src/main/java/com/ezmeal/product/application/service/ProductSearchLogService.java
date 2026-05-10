package com.ezmeal.product.application.service;

import com.ezmeal.product.application.request.ProductSearchRequest;
import com.ezmeal.product.application.search.log.ProductSearchLogAppender;
import com.ezmeal.product.application.search.log.ProductSearchLogCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchLogService {

    private final ProductSearchLogAppender productSearchLogAppender;

    public void saveIfNeeded(String userId, ProductSearchRequest request) {
        if (userId == null || !hasKeyword(request)) {
            return;
        }

        try {
            ProductSearchLogCommand command = ProductSearchLogCommand.from(userId, request);
            productSearchLogAppender.append(command);
        } catch (Exception e) {
            // 로그 저장 실패는 검색 응답을 막지 않음
            log.warn("검색 로그 저장 실패. userId={}, keyword={}", userId, request.keyword(), e);
        }
    }

    private boolean hasKeyword(ProductSearchRequest request) {
        return request != null && request.keyword() != null && !request.keyword().isBlank();
    }
}
