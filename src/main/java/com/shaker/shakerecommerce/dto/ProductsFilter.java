package com.shaker.shakerecommerce.dto;

public record ProductsFilter(
        String q,
        String sortBy,
        String sortOrder,
        String category,
        Integer pageSize,
        Integer pageNumber
) {
}
