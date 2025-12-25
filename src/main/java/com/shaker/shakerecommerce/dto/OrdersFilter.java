package com.shaker.shakerecommerce.dto;

import com.shaker.shakerecommerce.enums.OrderStatus;

public record OrdersFilter(
        OrderStatus status,
        String sortBy,
        String sortOrder,
        Integer pageSize,
        Integer pageNumber
) {
}