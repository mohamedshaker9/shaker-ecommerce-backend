package com.shaker.shakerecommerce.dto;

import com.shaker.shakerecommerce.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;

    private AddressType addressType;

    @NotBlank(message = "Street is required")
    @Size(min = 5, message = "Street must be at least 5 characters")
    private String street;

    @NotBlank(message = "Building number is required")
    @Size(min = 5, message = "Building number must be at least 5 characters")
    private String buildingNumber;

    @NotBlank(message = "City is required")
    @Size(min = 5, message = "City must be at least 5 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, message = "State must be at least 2 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(min = 2, message = "Country must be at least 2 characters")
    private String country;

    @NotBlank(message = "Zip code is required")
    @Size(min = 2, message = "Zip code must be at least 2 characters")
    private String zipCode;
}
