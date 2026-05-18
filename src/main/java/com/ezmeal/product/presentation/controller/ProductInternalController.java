package com.ezmeal.product.presentation.controller;

import com.ezmeal.common.response.CommonApiResponse;
import com.ezmeal.product.application.request.ProductOrderQuantityBulkReserveRequest;
import com.ezmeal.product.application.response.ProductInfo;
import com.ezmeal.product.application.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/order-quantity/reserve-bulk")
    public ResponseEntity<CommonApiResponse<Void>> reserveOrderQuantityBulk(
            @RequestBody @Valid ProductOrderQuantityBulkReserveRequest request
    ) {
        productService.reserveOrderQuantityBulk(request);
        return ResponseEntity.ok(CommonApiResponse.success());
    }

    @GetMapping("/by-ids")
    public List<ProductInfo> getProductsByIds(@RequestParam("ids") List<UUID> ids) {
        return productService.getProductsByIds(ids);
    }


}
