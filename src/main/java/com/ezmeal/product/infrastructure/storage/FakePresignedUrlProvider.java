package com.ezmeal.product.infrastructure.storage;


import com.ezmeal.product.application.upload.PresignedUrlProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class FakePresignedUrlProvider implements PresignedUrlProvider {

    private static final String MOCK_UPLOAD_BASE_URL = "http://localhost:19083/mock-upload/";

    @Override
    public String createUploadUrl(String objectKey, String contentType, long expiresInSeconds) {
        return MOCK_UPLOAD_BASE_URL + objectKey;
    }
}
