package com.ezmeal.product.presentation;

import com.ezmeal.common.response.CommonApiResponse;
import com.ezmeal.product.application.service.ProductService;
import com.ezmeal.product.application.request.ProductCreateRequest;
import com.ezmeal.product.application.request.ProductUpdateRequest;
import com.ezmeal.product.application.response.ProductResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<CommonApiResponse<ProductResponse>> createProduct(@RequestBody ProductCreateRequest productCreateRequest){

        ProductResponse response = productService.createProduct(productCreateRequest);

        return  ResponseEntity.status(HttpStatus.CREATED).body(CommonApiResponse.success("상품이 생성되었습니다.", response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CommonApiResponse<ProductResponse>> getProduct(@PathVariable UUID productId){
        ProductResponse response = productService.getProduct(productId);

        return ResponseEntity.ok(CommonApiResponse.success(("상품이 상세조회되었습니다."),response));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<CommonApiResponse<ProductResponse>> updateProduct(@PathVariable UUID productId, @RequestBody
                                                                            ProductUpdateRequest productUpdateRequest){
        ProductResponse response = productService.updateProduct(productId,productUpdateRequest);

        return ResponseEntity.ok(CommonApiResponse.success("상품이 수정되었습니다.", response));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonApiResponse<Void>> deleteProduct(@PathVariable UUID productId){
        productService.deleteProduct(productId, "SYSTEM");

        return ResponseEntity.ok(CommonApiResponse.success());
    }
}
