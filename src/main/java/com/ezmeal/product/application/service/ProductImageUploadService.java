package com.ezmeal.product.application.service;

import com.ezmeal.product.application.request.ProductImageUploadUrlRequest;
import com.ezmeal.product.application.response.ProductImageUploadUrlResponse;
import com.ezmeal.product.application.upload.PresignedUrlProvider;
import com.ezmeal.product.application.upload.ProductImageUploadType;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    private void validate(ProductImageUploadUrlRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Upload request is required.");
        }

        if (request.uploadType() == null) {
            throw new IllegalArgumentException("Upload type is required.");
        }

        if (request.productId() == null) {
            throw new IllegalArgumentException("Product id is required.");
        }

        if (!StringUtils.hasText(request.contentType())) {
            throw new IllegalArgumentException("Content type is required.");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(request.contentType())) {
            throw new IllegalArgumentException("Unsupported image content type.");
        }

        if (request.fileSize() == null) {
            throw new IllegalArgumentException("File size is required.");
        }

        if (request.fileSize() <= 0) {
            throw new IllegalArgumentException("File size must be positive.");
        }

        if (request.fileSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image file size must be 5MB or less.");
        }

        if (request.uploadType() == ProductImageUploadType.MEAL_PLAN_IMAGE
                && request.dayOfWeek() == null) {
            throw new IllegalArgumentException("Day of week is required for meal plan image.");
        }
    }
}
