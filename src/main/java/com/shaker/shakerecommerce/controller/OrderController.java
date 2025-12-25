package com.shaker.shakerecommerce.controller;


import com.shaker.shakerecommerce.dto.CreateOrderRequestDto;
import com.shaker.shakerecommerce.dto.OrderDto;
import com.shaker.shakerecommerce.dto.OrdersFilter;
import com.shaker.shakerecommerce.dto.PaginatedResponse;
import com.shaker.shakerecommerce.enums.OrderStatus;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequestDto request) throws ResourceNotFoundException {
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order,  HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<OrderDto>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNumber
    ) throws ResourceNotFoundException {

        OrdersFilter filter = new OrdersFilter(status, sortBy, sortOrder, pageSize, pageNumber);
        return ResponseEntity.ok(orderService.getOrders(filter));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) throws ResourceNotFoundException {
        OrderDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam("status") OrderStatus status
    ) throws ResourceNotFoundException {
        OrderDto updatedOrder = orderService.updateStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }




}