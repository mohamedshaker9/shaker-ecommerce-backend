package com.shaker.shakerecommerce.dto;

import java.util.Date;

public record AuthenticatedUserDto(
        String fullName,
        String username,
        String email,
        Date createdAt,
        Date updatedAt
) {
}
