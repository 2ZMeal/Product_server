package com.ezmeal.product.application.service;

import com.ezmeal.product.application.request.ProductSearchRequest;
import com.ezmeal.product.domain.event.payload.ProductSearchLoggedEvent;
import com.ezmeal.product.domain.event.producer.ProductSearchLogEventProducer;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchLogService {

    private final ProductSearchLogEventProducer productSearchLogEventProducer;

    public void saveIfNeeded(String userId, ProductSearchRequest request) {
        if (userId == null || !hasKeyword(request)) {
            return;
        }

        try {
            ProductSearchLoggedEvent event = ProductSearchLoggedEvent.of(userId,
                    normalize(request.keyword()),
                    normalize(request.category()),
                    normalize(request.mealPeriod()),
                    normalize(request.region()),
                    request.minPrice(),
                    request.maxPrice(),
                    LocalDateTime.now());
            productSearchLogEventProducer.publishSearchLoggedEvent(event);

            // 검색 로그 이벤트 발행 실패가 검색 응답에 영향을 주지 않도록 예외를 삼킨다.
        } catch (Exception e) {
            log.warn("검색 로그 이벤트 발행 실패.", e);
        }
    }

    private boolean hasKeyword(ProductSearchRequest request) {
        return request != null && request.keyword() != null && !request.keyword().isBlank();
    }

    private static String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
