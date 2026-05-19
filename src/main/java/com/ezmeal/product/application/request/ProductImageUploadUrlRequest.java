package com.ezmeal.product.application.request;

import com.ezmeal.product.application.upload.ProductImageUploadType;
import java.time.DayOfWeek;
import java.util.UUID;

public record ProductImageUploadUrlRequest(
        ProductImageUploadType uploadType,
        UUID productId,
        DayOfWeek dayOfWeek,
        String originalFileName,
        String contentType,
        Long fileSize
) {
}
