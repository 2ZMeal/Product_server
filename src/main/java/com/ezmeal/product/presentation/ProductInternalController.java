package com.ezmeal.product.presentation;

import com.ezmeal.common.response.CommonApiResponse;
import com.ezmeal.product.application.request.ProductOrderCountRequest;
import com.ezmeal.product.application.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/products")
public class ProductInternalController {

    private final ProductService productService;

    @PostMapping("/{productId}/order-quantity/reserve")
    public ResponseEntity<CommonApiResponse<Void>> reserveOrderQuantity(
            @PathVariable UUID productId,
            @RequestBody ProductOrderCountRequest request
    ) {
        productService.reserveOrderQuantity(productId, request.quantity());

        return ResponseEntity.ok(CommonApiResponse.success());
    }

    @PostMapping("/{productId}/order-quantity/restore")
    public ResponseEntity<CommonApiResponse<Void>> restoreOrderQuantity(
            @PathVariable UUID productId,
            @RequestBody ProductOrderCountRequest request
    ) {
        productService.restoreOrderQuantity(productId, request.quantity());

        return ResponseEntity.ok(CommonApiResponse.success());
    }
}
