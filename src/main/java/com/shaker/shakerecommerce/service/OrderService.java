package com.shaker.shakerecommerce.service;

import com.shaker.shakerecommerce.dto.*;
import com.shaker.shakerecommerce.enums.OrderStatus;
import com.shaker.shakerecommerce.exceptions.APIException;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.*;
import com.shaker.shakerecommerce.repo.*;
import com.shaker.shakerecommerce.shared.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final IOrderRepo orderRepo;
    private final CartRepo cartRepo;
    private final AddressRepo addressRepo;
    private final AuthUtils authUtils;
    private final ICartItemRepo cartItemRepo;

    @Transactional
    public OrderDto createOrder(CreateOrderRequestDto request) throws ResourceNotFoundException {
        User user = authUtils.getLoggedInUser();

        Cart cart = cartRepo.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getId()));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart", "items", "empty");
        }

        Address shippingAddress = addressRepo.findById(request.getShippingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", request.getShippingAddressId()));

        Order order = Order.builder()
                .user(user)
                .shippingAddress(shippingAddress)
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO)
                .paymentIntentId(request.getPaymentIntentId())
                .paymentType(request.getPaymentType())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Double priceValue = (cartItem.getProduct().getSpecialPrice() != null && cartItem.getProduct().getSpecialPrice() > 0)
                    ? cartItem.getProduct().getSpecialPrice()
                    : cartItem.getProduct().getPrice();
            BigDecimal price = BigDecimal.valueOf(priceValue);
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(price)
                    .subtotal(subtotal)
                    .build();

            order.addOrderItem(orderItem);
            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);


        Order savedOrder = orderRepo.save(order);


        // Remove existing items properly
        if (cart.getId() != null) {
            cartItemRepo.deleteByCartId(cart.getId());
            cart.getItems().clear();
        }
        cart.setTotal(0.0);
        cart.setDiscount(0.0);


        return mapToDto(savedOrder);
    }

    public List<OrderDto> getOrdersForCurrentUser() throws ResourceNotFoundException {
        User user = authUtils.getLoggedInUser();
        List<Order> orders = orderRepo.findByUserOrderByCreatedAtDesc(user);
        return orders.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Long orderId) throws ResourceNotFoundException {
        User user = authUtils.getLoggedInUser();
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        return mapToDto(order);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<OrderDto> getOrders(OrdersFilter filter) throws ResourceNotFoundException {

        int pageNumber = filter.pageNumber() == null ? 0 : Math.max(filter.pageNumber(), 0);
        int pageSize = filter.pageSize() == null ? 10 : Math.max(1, filter.pageSize());

        String sortBy = normalizeSortBy(filter.sortBy());
        Sort.Direction direction = parseDirection(filter.sortOrder());

        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        var spec = OrderSpecifications.withOptionalStatus(filter.status());

        Page<Order> orders = orderRepo.findAll(spec, pageable);

        return new PaginatedResponse<>(orders.getContent().stream().map(this::mapToDto).toList(),
                    orders.getNumber(),
                    orders.getNumberOfElements(),
                    orders.getTotalPages(),
                    orders.getTotalElements(),
                    orders.isLast()
                );
    }


    @Transactional
    public OrderDto updateStatus(Long orderId, OrderStatus status) throws ResourceNotFoundException {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() == status) {
            return mapToDto(order); // Idempotent: already in this state
        }

        if (!order.canTransitionTo(status)) {
            throw new APIException("Cannot transition order from " + order.getStatus() + " to " + status);
        }

        order.setStatus(status);
        Order updatedOrder = orderRepo.save(order);
        return mapToDto(updatedOrder);
    }

    private Sort.Direction parseDirection(String sortOrder) {
        if (sortOrder == null) return Sort.Direction.DESC;
        return "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    private String normalizeSortBy(String sortBy) {
        Set<String> allowed = Set.of("createdAt", "totalAmount", "status", "id");
        if (sortBy == null || sortBy.isBlank()) return "createdAt";
        return allowed.contains(sortBy) ? sortBy : "createdAt";
    }

    private OrderDto mapToDto(Order order) {
        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(item -> OrderItemDto.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(order.getId())
                .userEmail(order.getUser().getEmail())
                .fullName(order.getUser().getFullName())
                .shippingAddressId(order.getShippingAddress().getId())
                .orderItems(itemDtos)
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .paymentIntentId(order.getPaymentIntentId())
                .orderDate(order.getCreatedAt())
                .build();
    }



}