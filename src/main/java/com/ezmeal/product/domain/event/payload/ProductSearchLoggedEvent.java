package com.ezmeal.product.domain.event.payload;

import com.ezmeal.common.message.DomainEvent;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSearchLoggedEvent implements DomainEvent {

    private String userId;
    private String keyword;
    private String category;
    private String mealPeriod;
    private String region;
    private Integer minPrice;
    private Integer maxPrice;
    private LocalDateTime searchedAt;

    public static ProductSearchLoggedEvent of(
            String userId,
            String keyword,
            String category,
            String mealPeriod,
            String region,
            Integer minPrice,
            Integer maxPrice,
            LocalDateTime searchedAt
    ) {
        return new ProductSearchLoggedEvent(
                userId,
                keyword,
                category,
                mealPeriod,
                region,
                minPrice,
                maxPrice,
                searchedAt
        );
    }
}
