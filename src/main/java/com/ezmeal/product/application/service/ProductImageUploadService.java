package com.ezmeal.product.application.service;

import com.ezmeal.common.exception.CustomException;
import com.ezmeal.product.application.request.ProductImageUploadCompleteRequest;
import com.ezmeal.product.application.request.ProductImageUploadUrlRequest;
import com.ezmeal.product.application.response.ProductImageUploadUrlResponse;
import com.ezmeal.product.application.upload.PresignedUrlProvider;
import com.ezmeal.product.application.upload.ProductImageUploadType;
import com.ezmeal.product.domain.exception.ProductErrorCode;
import com.ezmeal.product.domain.model.product.Product;
import com.ezmeal.product.domain.repository.product.ProductRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProductImageUploadService {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024L;
    private static final long EXPIRES_IN_SECONDS = 600L;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final ProductImageKeyGenerator productImageKeyGenerator;
    private final PresignedUrlProvider presignedUrlProvider;
    private final ProductRepository productRepository;

    public ProductImageUploadUrlResponse createUploadUrl(ProductImageUploadUrlRequest request) {
        validate(request);

        String objectKey = productImageKeyGenerator.generate(request);
        String uploadUrl = presignedUrlProvider.createUploadUrl(
                objectKey,
                request.contentType(),
                EXPIRES_IN_SECONDS
        );

        return new ProductImageUploadUrlResponse(
                uploadUrl,
                objectKey,
                "PUT",
                request.contentType(),
                EXPIRES_IN_SECONDS
        );
    }

    @Transactional
    public void completeUpload(ProductImageUploadCompleteRequest request) {
        validateCompleteRequest(request);

        Product product = productRepository.findByIdAndDeletedAtIsNull(request.productId())
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        validateObjectKeyMatchesRequest(request);
        try {
            if (request.uploadType() == ProductImageUploadType.PRODUCT_MAIN_IMAGE) {
                product.updateMainImageKey(request.objectKey());
                return;
            }

            if (request.uploadType() == ProductImageUploadType.MEAL_PLAN_IMAGE) {
                product.updateMealPlanImageKey(request.dayOfWeek(), request.objectKey());
                return;
            }

        } catch (IllegalArgumentException e) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
    }

    private void validate(ProductImageUploadUrlRequest request) {
        if (request == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (request.uploadType() == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (request.productId() == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (!StringUtils.hasText(request.contentType())) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (!ALLOWED_CONTENT_TYPES.contains(request.contentType())) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_UNSUPPORTED_CONTENT_TYPE);
        }

        if (request.fileSize() == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (request.fileSize() <= 0) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (request.fileSize() > MAX_IMAGE_SIZE) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_SIZE_EXCEEDED);
        }

        if (request.uploadType() == ProductImageUploadType.MEAL_PLAN_IMAGE
                && request.dayOfWeek() == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }
    }

    private void validateCompleteRequest(ProductImageUploadCompleteRequest request) {
        if (request == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (request.uploadType() == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (request.productId() == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (!StringUtils.hasText(request.objectKey())) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }

        if (request.uploadType() == ProductImageUploadType.MEAL_PLAN_IMAGE
                && request.dayOfWeek() == null) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_INVALID_REQUEST);
        }
    }

    private void validateObjectKeyMatchesRequest(ProductImageUploadCompleteRequest request) {
        String expectedPrefix = switch (request.uploadType()) {
            case PRODUCT_MAIN_IMAGE -> "products/%s/main/".formatted(request.productId());
            case MEAL_PLAN_IMAGE -> "products/%s/meal-plans/%s/".formatted(
                    request.productId(),
                    request.dayOfWeek()
            );
        };

        if (!request.objectKey().startsWith(expectedPrefix)) {
            throw new CustomException(ProductErrorCode.PRODUCT_IMAGE_OBJECT_KEY_MISMATCH);
        }
    }
}
