package com.shaker.shakerecommerce.controller;

import com.shaker.shakerecommerce.dto.StripePaymentDto;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.service.StripePaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {


    private final StripePaymentService stripePaymentService;

    public PaymentController(StripePaymentService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody StripePaymentDto stripePaymentDto) throws StripeException, ResourceNotFoundException {
       return new ResponseEntity<>(stripePaymentService.getClientSecret(stripePaymentDto), HttpStatus.CREATED);
    }


}
