package com.ezmeal.product.application.service;

import com.ezmeal.product.application.request.ProductImageUploadUrlRequest;
import com.ezmeal.product.application.upload.ProductImageUploadType;
import java.time.DayOfWeek;
import java.util.UUID;
import org.springframework.stereotype.Component;


@Component
public class ProductImageKeyGenerator {

    public String generate(ProductImageUploadUrlRequest request) {
        String extension = resolveExtension(request.contentType());
        String fileId = UUID.randomUUID().toString();

        if (request.uploadType() == ProductImageUploadType.PRODUCT_MAIN_IMAGE) {
            return "products/%s/main/%s.%s".formatted(
                    request.productId(),
                    fileId,
                    extension
            );
        }

        if (request.uploadType() == ProductImageUploadType.MEAL_PLAN_IMAGE) {
            DayOfWeek dayOfWeek = request.dayOfWeek();

            return "products/%s/meal-plans/%s/%s.%s".formatted(
                    request.productId(),
                    dayOfWeek,
                    fileId,
                    extension
            );
        }

        throw new IllegalArgumentException("Unsupported product image upload type.");
    }

    private String resolveExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new IllegalArgumentException("Unsupported image content type.");
        };
    }
}
