package com.ezmeal.product.application.upload;

public interface PresignedUrlProvider {
    String createUploadUrl(String objectKey, String contentType, long expiresInSeconds);
}
