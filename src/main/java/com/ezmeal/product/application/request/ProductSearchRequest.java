package com.ezmeal.product.application.request;

public record ProductSearchRequest(
        String keyword,
        String category,
        String mealPeriod,
        String dayOfWeek,
        String region,
        Integer minPrice,
        Integer maxPrice,
        Boolean available,
        Integer page,
        Integer size,
        String sortBy,
        String direction) {
}
