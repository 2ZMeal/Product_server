package com.ezmeal.product.domain.event.payload;

import com.ezmeal.product.domain.event.ProductEventType;
import com.ezmeal.product.domain.model.product.ProductCategory;
import com.ezmeal.product.domain.model.product.ProductMealPeriod;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductUpdatedEvent {

    private UUID eventId;
    private ProductEventType productEventType;
    private OffsetDateTime occurredAt;
    private UUID productId;
    private UUID companyId;
    private String productName;
    private String productDescription;
    private Integer price;
    private ProductCategory category;
    private ProductMealPeriod mealPeriod;
    private Integer maxOrderCount;
    private List<ProductMealPlanEventPayload> mealPlans;

    public static ProductUpdatedEvent of(UUID productId, UUID companyId, String productName, String productDescription,
                                         Integer price,
                                         ProductCategory category, ProductMealPeriod mealPeriod, Integer maxOrderCount,
                                         List<ProductMealPlanEventPayload> mealPlans
    ) {
        return new ProductUpdatedEvent(UUID.randomUUID(),
                ProductEventType.PRODUCT_UPDATED,
                OffsetDateTime.now(), productId, companyId, productName, productDescription, price, category,
                mealPeriod,
                maxOrderCount, mealPlans);
    }
}
