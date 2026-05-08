package com.ezmeal.product.presentation;

import com.ezmeal.common.response.CommonApiResponse;
import com.ezmeal.product.application.request.ProductOrderCountRequest;
import com.ezmeal.product.application.response.ProductInfo;
import com.ezmeal.product.application.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/products")
public class ProductInternalController {

    private final ProductService productService;

    @PostMapping("/{productId}/order-quantity/reserve")
    public ResponseEntity<CommonApiResponse<Void>> reserveOrderQuantity(
            @PathVariable UUID productId,
            @RequestBody @Valid ProductOrderCountRequest request
    ) {
        productService.reserveOrderQuantity(productId, request.orderId(), request.quantity());

        return ResponseEntity.ok(CommonApiResponse.success());
    }


    // api 테스트용
    @PostMapping("/{productId}/order-quantity/restore")
    public ResponseEntity<CommonApiResponse<Void>> restoreOrderQuantity(
            @PathVariable UUID productId,
            @RequestBody ProductOrderCountRequest request
    ) {
        productService.restoreOrderQuantity(productId, request.quantity());

        return ResponseEntity.ok(CommonApiResponse.success());
    }

    @GetMapping("/by-ids")
    public List<ProductInfo> getProductsByIds(@RequestParam("ids") List<UUID> ids) {
        return productService.getProductsByIds(ids);
    }


}
