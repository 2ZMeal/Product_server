package com.ezmeal.product.presentation.controller;

import com.ezmeal.common.response.CommonApiResponse;
import com.ezmeal.product.application.request.ProductImageUploadCompleteRequest;
import com.ezmeal.product.application.request.ProductImageUploadUrlRequest;
import com.ezmeal.product.application.response.ProductImageUploadUrlResponse;
import com.ezmeal.product.application.service.ProductImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/images")
public class ProductImageUploadController {

    private final ProductImageUploadService productImageUploadService;

    @PostMapping("/upload-url")
    public ResponseEntity<CommonApiResponse<ProductImageUploadUrlResponse>> createUploadUrl(
            @RequestBody ProductImageUploadUrlRequest request
    ) {
        ProductImageUploadUrlResponse response = productImageUploadService.createUploadUrl(request);

        return ResponseEntity.ok(
                CommonApiResponse.success("이미지 업로드 URL을 발급했습니다.", response)
        );
    }

    @PostMapping("/complete")
    public ResponseEntity<CommonApiResponse<Void>> completeUpload(
            @RequestBody ProductImageUploadCompleteRequest request
    ) {
        productImageUploadService.completeUpload(request);

        return ResponseEntity.ok(CommonApiResponse.success());
    }
}
