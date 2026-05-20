package com.ezmeal.product.application.upload;

import com.ezmeal.product.infrastructure.config.AwsProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProductImageUrlResolver {

    private final String imageBaseUrl;

    public ProductImageUrlResolver(AwsProperties awsProperties) {
        this.imageBaseUrl = removeTrailingSlash(awsProperties.s3().imageBaseUrl());
    }

    public String resolve(String objectKey) {
        if (!StringUtils.hasText(objectKey)) {
            return null;
        }

        return imageBaseUrl + "/" + objectKey;
    }

    private String removeTrailingSlash(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }

        return value;
    }
}
