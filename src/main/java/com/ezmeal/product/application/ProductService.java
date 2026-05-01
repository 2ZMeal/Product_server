package com.ezmeal.product.application;

import com.ezmeal.common.exception.CustomException;
import com.ezmeal.product.application.request.ProductCreateRequest;
import com.ezmeal.product.application.request.ProductMealPlanCreateRequest;
import com.ezmeal.product.application.request.ProductMealPlanUpdateRequest;
import com.ezmeal.product.application.request.ProductUpdateRequest;
import com.ezmeal.product.application.response.ProductResponse;
import com.ezmeal.product.domain.event.payload.ProductCreatedEvent;
import com.ezmeal.product.domain.event.payload.ProductDeletedEvent;
import com.ezmeal.product.domain.event.payload.ProductMealPlanEventPayload;
import com.ezmeal.product.domain.event.payload.ProductUpdatedEvent;
import com.ezmeal.product.domain.exception.ProductErrorCode;
import com.ezmeal.product.domain.model.Product;
import com.ezmeal.product.domain.model.ProductMealPlan;
import com.ezmeal.product.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    //상품 생성
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {

        if (productCreateRequest.mealPlans() == null || productCreateRequest.mealPlans().isEmpty()) {
            throw new CustomException(ProductErrorCode.PRODUCT_MEAL_PLAN_REQUIRED);
        }

        Product product = new Product(productCreateRequest.companyId(), productCreateRequest.name(),
                productCreateRequest.description(),
                productCreateRequest.price(), productCreateRequest.maxOrderCount(), productCreateRequest.category(),
                productCreateRequest.mealPeriod());

        for (ProductMealPlanCreateRequest mealPlanRequest : productCreateRequest.mealPlans()) {
            ProductMealPlan mealPlan = new ProductMealPlan(
                    mealPlanRequest.dayOfWeek(),
                    mealPlanRequest.menuName(),
                    mealPlanRequest.allergyInfo(),
                    mealPlanRequest.nutritionInfo()
            );

            product.addMealPlan(mealPlan);
        }
        Product productSaved = productRepository.save(product);

        List<ProductMealPlanEventPayload> mealPlans = productSaved.getMealPlans().stream()
                .map(ProductMealPlanEventPayload::from)
                .toList();

        ProductCreatedEvent event = ProductCreatedEvent.of(productSaved.getId(), productSaved.getCompanyId(),
                productSaved.getName(),
                productSaved.getDescription(),
                productSaved.getPrice(), productSaved.getCategory(), productSaved.getMealPeriod(),
                productSaved.getMaxOrderCount(), mealPlans
        );
        applicationEventPublisher.publishEvent(event);

        return ProductResponse.from(productSaved);
    }

    //상품 수정(요일별 식단 개별 수정x 전체 수정o)
    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductUpdateRequest productUpdateRequest) {

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.update(productUpdateRequest.companyId(), productUpdateRequest.name(),
                productUpdateRequest.description(),
                productUpdateRequest.price(), productUpdateRequest.maxOrderCount(), productUpdateRequest.category(),
                productUpdateRequest.mealPeriod());

        if (productUpdateRequest.mealPlans() != null) {
            if (productUpdateRequest.mealPlans().isEmpty()) {
                throw new CustomException(ProductErrorCode.PRODUCT_MEAL_PLAN_REQUIRED);
            }
            List<ProductMealPlan> newMealPlans = new ArrayList<>();

            for (ProductMealPlanUpdateRequest mealPlanRequest : productUpdateRequest.mealPlans()) {
                ProductMealPlan mealPlan = new ProductMealPlan(
                        mealPlanRequest.dayOfWeek(),
                        mealPlanRequest.menuName(),
                        mealPlanRequest.allergyInfo(),
                        mealPlanRequest.nutritionInfo()
                );

                newMealPlans.add(mealPlan);
            }

            product.replaceMealPlans(newMealPlans);
        }

        List<ProductMealPlanEventPayload> mealPlans = product.getMealPlans().stream()
                .map(ProductMealPlanEventPayload::from)
                .toList();

        ProductUpdatedEvent event = ProductUpdatedEvent.of(product.getId(), product.getCompanyId(), product.getName(),
                product.getDescription(),
                product.getPrice(), product.getCategory(), product.getMealPeriod(), product.getMaxOrderCount(),
                mealPlans
        );
        applicationEventPublisher.publishEvent(event);

        return ProductResponse.from(product);
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(UUID productId, String deletedBy) {

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.delete(deletedBy);

        ProductDeletedEvent event = ProductDeletedEvent.of(product.getId(), product.getCompanyId());
        applicationEventPublisher.publishEvent(event);
    }

    //단건 조회
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID productId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return ProductResponse.from(product);
    }

}
