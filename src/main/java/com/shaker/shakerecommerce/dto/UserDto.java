package com.shaker.shakerecommerce.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

public record UserDto(
        String fullName,
        String username,
        String email,
        Date createdAt,
        Date updatedAt
) {
}
