package com.ezmeal.product.application.upload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProductImageUrlResolver {

    private final String imageBaseUrl;

    public ProductImageUrlResolver(
            @Value("${cloud.aws.s3.image-base-url}") String imageBaseUrl
    ) {
        this.imageBaseUrl = removeTrailingSlash(imageBaseUrl);
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
