package com.ezmeal.product.infrastructure.storage;

import com.ezmeal.product.application.upload.PresignedUrlProvider;
import com.ezmeal.product.infrastructure.config.AwsProperties;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@Profile("s3")
@RequiredArgsConstructor
public class S3PresignedUrlProvider implements PresignedUrlProvider {

    private final S3Presigner s3Presigner;
    private final AwsProperties awsProperties;

    @Override
    public String createUploadUrl(String objectKey, String contentType, long expiresInSeconds) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.s3().bucket())
                .key(objectKey)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expiresInSeconds))
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest)
                .url()
                .toString();
    }
}
