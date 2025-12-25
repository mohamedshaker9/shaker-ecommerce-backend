package com.shaker.shakerecommerce.controller;

import com.shaker.shakerecommerce.dto.AddressDto;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressDto> saveAddress(
            @Valid @RequestBody AddressDto addressDto) throws ResourceNotFoundException {

        AddressDto savedAddress = addressService.save(addressDto);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> get() throws ResourceNotFoundException {

        List<AddressDto> addresses = addressService.get();
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressDto addressDto) throws ResourceNotFoundException {
        AddressDto updatedAddress = addressService.update(addressId, addressDto);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) throws ResourceNotFoundException {
        addressService.delete(addressId);
        return ResponseEntity.noContent().build();
    }
}