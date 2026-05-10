package com.ezmeal.product.presentation;

import com.ezmeal.common.response.CommonApiResponse;
import com.ezmeal.common.security.principal.CustomUserPrincipal;
import com.ezmeal.product.application.request.ProductCreateRequest;
import com.ezmeal.product.application.request.ProductSearchRequest;
import com.ezmeal.product.application.request.ProductUpdateRequest;
import com.ezmeal.product.application.response.PageResponse;
import com.ezmeal.product.application.response.ProductResponse;
import com.ezmeal.product.application.response.ProductSearchResponse;
import com.ezmeal.product.application.service.ProductSearchService;
import com.ezmeal.product.application.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final ProductSearchService productSearchService;


    @PostMapping
    public ResponseEntity<CommonApiResponse<ProductResponse>> createProduct(
            @RequestBody ProductCreateRequest productCreateRequest,
            Authentication authentication) {

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        ProductResponse response = productService.createProduct(productCreateRequest, principal.getUserId(),
                principal.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonApiResponse.success("상품이 생성되었습니다.", response));
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<PageResponse<ProductSearchResponse>>> searchProducts(
            @ModelAttribute ProductSearchRequest request,
            Authentication authentication
    ) {
        String userId = null;

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            userId = principal.getUserId();
        }

        PageResponse<ProductSearchResponse> response = productSearchService.searchProducts(userId,
                request);

        return ResponseEntity.ok(CommonApiResponse.success("상품 목록을 조회했습니다.", response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CommonApiResponse<ProductResponse>> getProduct(@PathVariable UUID productId) {
        ProductResponse response = productService.getProduct(productId);

        return ResponseEntity.ok(CommonApiResponse.success(("상품이 상세조회되었습니다."), response));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<CommonApiResponse<ProductResponse>> updateProduct(@PathVariable UUID productId, @RequestBody
                                                                            ProductUpdateRequest productUpdateRequest,
                                                                            Authentication authentication) {

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        ProductResponse response = productService.updateProduct(productId, productUpdateRequest, principal.getUserId(),
                principal.getRole());

        return ResponseEntity.ok(CommonApiResponse.success("상품이 수정되었습니다.", response));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonApiResponse<Void>> deleteProduct(@PathVariable UUID productId,
                                                                 Authentication authentication) {

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        productService.deleteProduct(productId, principal.getUserId(), principal.getRole());

        return ResponseEntity.ok(CommonApiResponse.success());
    }
}
