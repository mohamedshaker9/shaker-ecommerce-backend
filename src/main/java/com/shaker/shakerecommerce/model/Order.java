package com.shaker.shakerecommerce.model;

import com.shaker.shakerecommerce.enums.OrderStatus;
import com.shaker.shakerecommerce.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;


    @Column(nullable = false)
    private BigDecimal totalAmount;

    private String paymentIntentId;


    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.setOrder(this);
    }


    public boolean canTransitionTo(OrderStatus nextStatus) {
        return switch (this.status) {
            case CREATED -> List.of(OrderStatus.PAYMENT_SUCCESS, OrderStatus.PAYMENT_FAILED, OrderStatus.CANCELLED).contains(nextStatus);
            case PAYMENT_SUCCESS -> List.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED, OrderStatus.REFUNDED).contains(nextStatus);
            case SHIPPED -> List.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED, OrderStatus.REFUNDED).contains(nextStatus);
            case PAYMENT_FAILED, DELIVERED, CANCELLED, REFUNDED -> false;
        };
    }

}