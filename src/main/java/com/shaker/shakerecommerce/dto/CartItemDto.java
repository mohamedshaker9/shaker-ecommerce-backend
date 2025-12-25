package com.shaker.shakerecommerce.dto;

import java.util.List;

public record CartItemDto (
         int id,
         ProductDTO productDTO,
         Integer quantity,
         double discount,
         double price,
         double totalPrice

){
}
