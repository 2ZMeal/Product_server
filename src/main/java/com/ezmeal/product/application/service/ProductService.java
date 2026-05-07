package com.ezmeal.product.application.service;

import com.ezmeal.common.enums.Role;
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
import com.ezmeal.product.domain.model.companySnapShot.CompanySnapshot;
import com.ezmeal.product.domain.model.product.Product;
import com.ezmeal.product.domain.model.product.ProductMealPlan;
import com.ezmeal.product.domain.model.productReservation.ProductReservation;
import com.ezmeal.product.domain.repository.companySnapshot.CompanySnapshotRepository;
import com.ezmeal.product.domain.repository.product.ProductRepository;
import com.ezmeal.product.domain.repository.productReservation.ProductReservationRepository;
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
    private final CompanySnapshotRepository companySnapshotRepository;
    private final ProductReservationRepository productReservationRepository;

    //상품 생성
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest, String userId, Role role) {

        CompanySnapshot companySnapshot = companySnapshotRepository.findByCompanyIdAndDeletedAtIsNull(
                        productCreateRequest.companyId())
                .orElseThrow(() -> new CustomException(ProductErrorCode.COMPANY_NOT_FOUND));

        validateCompanyAccess(companySnapshot, userId, role);

        if (productCreateRequest.mealPlans() == null || productCreateRequest.mealPlans().isEmpty()) {
            throw new CustomException(ProductErrorCode.PRODUCT_MEAL_PLAN_REQUIRED);
        }

        Product product = new Product(productCreateRequest.companyId(), productCreateRequest.name(),
                productCreateRequest.description(), productCreateRequest.price(), productCreateRequest.maxOrderCount(),
                productCreateRequest.category(), productCreateRequest.mealPeriod());

        for (ProductMealPlanCreateRequest mealPlanRequest : productCreateRequest.mealPlans()) {
            ProductMealPlan mealPlan = new ProductMealPlan(mealPlanRequest.dayOfWeek(), mealPlanRequest.menuName(),
                    mealPlanRequest.allergyInfo(), mealPlanRequest.nutritionInfo());

            product.addMealPlan(mealPlan);
        }
        Product productSaved = productRepository.save(product);

        List<ProductMealPlanEventPayload> mealPlans = productSaved.getMealPlans().stream()
                .map(ProductMealPlanEventPayload::from).toList();

        ProductCreatedEvent event = ProductCreatedEvent.of(productSaved.getId(), productSaved.getCompanyId(),
                productSaved.getName(), productSaved.getDescription(), productSaved.getPrice(),
                productSaved.getCategory(), productSaved.getMealPeriod(), productSaved.getMaxOrderCount(), mealPlans,
                productSaved.getCreatedAt(), productSaved.getModifiedAt());
        applicationEventPublisher.publishEvent(event);

        return ProductResponse.from(productSaved);
    }

    //상품 수정(요일별 식단 개별 수정x 전체 수정o)
    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductUpdateRequest productUpdateRequest, String userId,
                                         Role role) {

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        CompanySnapshot currentCompanySnapshot = companySnapshotRepository.findByCompanyIdAndDeletedAtIsNull(
                product.getCompanyId()).orElseThrow(() -> new CustomException(ProductErrorCode.COMPANY_NOT_FOUND));

        validateCompanyAccess(currentCompanySnapshot, userId, role);

        product.update(null, productUpdateRequest.name(), productUpdateRequest.description(),
                productUpdateRequest.price(), productUpdateRequest.maxOrderCount(), productUpdateRequest.category(),
                productUpdateRequest.mealPeriod());

        if (productUpdateRequest.mealPlans() != null) {
            if (productUpdateRequest.mealPlans().isEmpty()) {
                throw new CustomException(ProductErrorCode.PRODUCT_MEAL_PLAN_REQUIRED);
            }
            List<ProductMealPlan> newMealPlans = new ArrayList<>();

            for (ProductMealPlanUpdateRequest mealPlanRequest : productUpdateRequest.mealPlans()) {
                ProductMealPlan mealPlan = new ProductMealPlan(mealPlanRequest.dayOfWeek(), mealPlanRequest.menuName(),
                        mealPlanRequest.allergyInfo(), mealPlanRequest.nutritionInfo());

                newMealPlans.add(mealPlan);
            }

            product.replaceMealPlans(newMealPlans);
        }

        List<ProductMealPlanEventPayload> mealPlans = product.getMealPlans().stream()
                .map(ProductMealPlanEventPayload::from).toList();

        ProductUpdatedEvent event = ProductUpdatedEvent.of(product.getId(), product.getCompanyId(), product.getName(),
                product.getDescription(), product.getPrice(), product.getCategory(), product.getMealPeriod(),
                product.getMaxOrderCount(), mealPlans, product.getCreatedAt(), product.getModifiedAt());
        applicationEventPublisher.publishEvent(event);

        return ProductResponse.from(product);
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(UUID productId, String userId, Role role) {

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        CompanySnapshot companySnapshot = companySnapshotRepository.findByCompanyIdAndDeletedAtIsNull(
                product.getCompanyId()).orElseThrow(() -> new CustomException(ProductErrorCode.COMPANY_NOT_FOUND));

        validateCompanyAccess(companySnapshot, userId, role);

        product.delete(userId);

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

    private void validateCompanyAccess(CompanySnapshot companySnapshot, String userId, Role role) {
        if (role == Role.ADMIN) {
            return;
        }

        if (role != Role.COMPANY) {
            throw new CustomException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }

        if (!companySnapshot.getManagerUserId().equals(parseUserId(userId))) {
            throw new CustomException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }
    }

    private UUID parseUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }
    }

    @Transactional
    public void reserveOrderQuantity(UUID productId, UUID orderId, Integer quantity) {

        validateReservationRequest(orderId);
        validateOrderQuantity(quantity);

        if (productReservationRepository.existsByOrderIdAndProductId(orderId, productId)) {
            return;
        }

        Product product = productRepository.findByIdAndDeletedAtIsNullForUpdate(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (product.getMaxOrderCount() < quantity) {
            throw new CustomException(ProductErrorCode.PRODUCT_ORDER_QUANTITY_EXCEEDED);
        }

        product.reserveOrderQuantity(quantity);

        productReservationRepository.save(
                new ProductReservation(orderId, productId, quantity)
        );

    }

    @Transactional
    public void restoreReservedQuantity(UUID productId, UUID orderId) {
        validateReservationRequest(orderId);

        ProductReservation reservation = productReservationRepository.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (reservation.isRestored()) {
            return;
        }

        Product product = productRepository.findByIdAndDeletedAtIsNullForUpdate(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.restoreOrderQuantity(reservation.getQuantity());
        reservation.restore();
    }

    //api 테스트용
    @Transactional
    public void restoreOrderQuantity(UUID productId, Integer quantity) {
        Product product = productRepository.findByIdAndDeletedAtIsNullForUpdate(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        validateOrderQuantity(quantity);

        product.restoreOrderQuantity(quantity);
    }

    private void validateOrderQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new CustomException(ProductErrorCode.PRODUCT_ORDER_QUANTITY_INVALID);
        }
    }

    private void validateReservationRequest(UUID orderId) {
        if (orderId == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_INVALID_REQUEST);
        }
    }

}
