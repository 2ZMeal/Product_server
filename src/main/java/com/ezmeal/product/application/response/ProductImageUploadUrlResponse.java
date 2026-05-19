package com.ezmeal.product.application.response;

public record ProductImageUploadUrlResponse(
        String uploadUrl,
        String objectKey,
        String method,
        String contentType,
        Long expiresInSeconds
) {
}
