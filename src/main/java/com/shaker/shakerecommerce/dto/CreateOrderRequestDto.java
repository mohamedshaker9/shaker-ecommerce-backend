package com.shaker.shakerecommerce.dto;

import com.shaker.shakerecommerce.enums.PaymentType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequestDto {
    private Long shippingAddressId;
    private String paymentIntentId;
    private PaymentType paymentType;

}