package com.shaker.shakerecommerce.dto;

import java.util.List;

public record CartDto(
        Long id,
        List<CartItemDto> cartDtoList,
        double subtotal,
        double discount
) {
}
