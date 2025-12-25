package com.shaker.shakerecommerce.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class StripePaymentDto {

    private Double amount;
    private String currency;
    private Long selectedShippingAddressId;
}
