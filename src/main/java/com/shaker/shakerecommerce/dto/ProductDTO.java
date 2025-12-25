package com.shaker.shakerecommerce.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class ProductDTO {

    private Long id;
    private String productName;
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
    private String image;
    private String category;
    private Long categoryId;
}
