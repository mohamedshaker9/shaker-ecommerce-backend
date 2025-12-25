package com.shaker.shakerecommerce.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CategoryDTO {

    private Long id;

    @NotBlank
    @Size(min = 5)
    private String name;

}
