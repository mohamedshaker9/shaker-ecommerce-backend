package com.shaker.shakerecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shaker.shakerecommerce.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String userEmail;
    private String fullName;
    private Long shippingAddressId;
    private List<OrderItemDto> orderItems;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String paymentIntentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime orderDate;
}