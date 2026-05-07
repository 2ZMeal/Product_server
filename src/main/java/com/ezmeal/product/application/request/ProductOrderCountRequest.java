package com.ezmeal.product.application.request;

import java.util.UUID;

public record ProductOrderCountRequest(
        UUID orderId,
        Integer quantity
) {
}
