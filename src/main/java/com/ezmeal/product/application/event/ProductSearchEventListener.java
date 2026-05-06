package com.ezmeal.product.application.event;

import com.ezmeal.common.exception.CustomException;
import com.ezmeal.product.application.search.ProductDeliveryAreaSearchCommand;
import com.ezmeal.product.application.search.ProductMealPlanSearchCommand;
import com.ezmeal.product.application.search.ProductSearchIndexCommand;
import com.ezmeal.product.application.search.ProductSearchIndexer;
import com.ezmeal.product.domain.event.payload.ProductCreatedEvent;
import com.ezmeal.product.domain.event.payload.ProductDeletedEvent;
import com.ezmeal.product.domain.event.payload.ProductMealPlanEventPayload;
import com.ezmeal.product.domain.event.payload.ProductUpdatedEvent;
import com.ezmeal.product.domain.exception.ProductErrorCode;
import com.ezmeal.product.domain.model.companySnapShot.CompanySnapshot;
import com.ezmeal.product.domain.repository.companySnapshot.CompanySnapshotRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProductSearchEventListener {

    private final ProductSearchIndexer productSearchIndexer;
    private final CompanySnapshotRepository companySnapshotRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreatedEvent event) {
        ProductSearchIndexCommand command = toCommand(event);
        productSearchIndexer.save(command);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductUpdated(ProductUpdatedEvent event) {
        ProductSearchIndexCommand command = toCommand(event);
        productSearchIndexer.save(command);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductDeleted(ProductDeletedEvent event) {
        productSearchIndexer.delete(event.getProductId().toString());
    }

    private ProductSearchIndexCommand toCommand(ProductCreatedEvent event) {
        return toCommand(
                event.getProductId(),
                event.getCompanyId(),
                event.getProductName(),
                event.getProductDescription(),
                event.getPrice(),
                event.getMaxOrderCount(),
                event.getCategory().name(),
                event.getMealPeriod().name(),
                event.getMealPlans(),
                event.getCreatedAt(),
                event.getModifiedAt()
        );
    }

    private ProductSearchIndexCommand toCommand(ProductUpdatedEvent event) {
        return toCommand(
                event.getProductId(),
                event.getCompanyId(),
                event.getProductName(),
                event.getProductDescription(),
                event.getPrice(),
                event.getMaxOrderCount(),
                event.getCategory().name(),
                event.getMealPeriod().name(),
                event.getMealPlans(),
                event.getCreatedAt(),
                event.getModifiedAt()
        );
    }

    private ProductSearchIndexCommand toCommand(
            UUID productId,
            UUID companyId,
            String productName,
            String productDescription,
            Integer price,
            Integer maxOrderCount,
            String category,
            String mealPeriod,
            List<ProductMealPlanEventPayload> eventMealPlans,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        CompanySnapshot companySnapshot = companySnapshotRepository
                .findWithDeliveryAreasByCompanyIdAndDeletedAtIsNull(companyId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.COMPANY_NOT_FOUND));

        List<ProductMealPlanSearchCommand> mealPlans =
                eventMealPlans == null ? List.of() : eventMealPlans.stream()
                        .map(mealPlan -> new ProductMealPlanSearchCommand(
                                mealPlan.dayOfWeek().name(),
                                mealPlan.menuName(),
                                mealPlan.allergyInfo(),
                                mealPlan.nutritionInfo()
                        ))
                        .toList();

        List<String> availableDays =
                mealPlans.stream()
                        .map(ProductMealPlanSearchCommand::dayOfWeek)
                        .distinct()
                        .toList();

        List<String> menuNames = mealPlans.stream()
                .map(ProductMealPlanSearchCommand::menuName)
                .toList();

        List<ProductDeliveryAreaSearchCommand> deliveryAreas = companySnapshot.getDeliveryAreas().stream()
                .map(deliveryArea -> new ProductDeliveryAreaSearchCommand(
                        deliveryArea.getRegion(),
                        deliveryArea.getMealPeriod(),
                        deliveryArea.getEstimatedArrivalStartTime().toString(),
                        deliveryArea.getEstimatedArrivalEndTime().toString(),
                        toMinute(deliveryArea.getEstimatedArrivalStartTime()),
                        toMinute(deliveryArea.getEstimatedArrivalEndTime())
                ))
                .toList();

        List<String> deliveryRegions = deliveryAreas.stream()
                .map(ProductDeliveryAreaSearchCommand::region)
                .distinct()
                .toList();

        return new ProductSearchIndexCommand(
                productId.toString(),
                companyId.toString(),
                companySnapshot.getName(),
                companySnapshot.getDescription(),
                productName,
                productDescription,
                price,
                maxOrderCount,
                maxOrderCount != null && maxOrderCount > 0,
                category,
                mealPeriod,
                availableDays,
                menuNames,
                deliveryRegions,
                mealPlans,
                deliveryAreas,
                createdAt,
                modifiedAt

        );
    }

    private int toMinute(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }
}
