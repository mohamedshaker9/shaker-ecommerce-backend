package com.shaker.shakerecommerce.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDto (
        @NotBlank @Size(min= 5, max = 30) String fullName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank @Size(min = 5) String username,
        boolean isSeller){}



