
package com.shaker.shakerecommerce.service;

import com.shaker.shakerecommerce.dto.AddressDto;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.Address;
import com.shaker.shakerecommerce.model.User;
import com.shaker.shakerecommerce.repo.AddressRepo;
import com.shaker.shakerecommerce.repo.UserRepo;
import com.shaker.shakerecommerce.shared.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepo addressRepo;
    private final AuthUtils authUtils;
    private final ModelMapper modelMapper;

    public AddressService(AddressRepo addressRepo, UserRepo userRepo, AuthUtils authUtils, ModelMapper modelMapper) {
        this.addressRepo = addressRepo;
        this.authUtils = authUtils;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public AddressDto save(AddressDto addressDto) throws ResourceNotFoundException {
        User user = authUtils.getLoggedInUser();

        Address address = modelMapper.map(addressDto, Address.class);
        address.setUser(user);

        Address savedAddress = addressRepo.save(address);

        return modelMapper.map(savedAddress, AddressDto.class);
    }

    public List<AddressDto> get() throws ResourceNotFoundException {
        Long userId = authUtils.getLoggedInUserId();

        List<Address> addresses = addressRepo.findByUserId(userId);
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();
    }

    @Transactional
    public AddressDto update(Long addressId, AddressDto addressDto) throws ResourceNotFoundException {
        Long userId = authUtils.getLoggedInUserId();

        Address address = addressRepo.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        modelMapper.map(addressDto, address);

        Address updatedAddress = addressRepo.save(address);
        return modelMapper.map(updatedAddress, AddressDto.class);
    }

    @Transactional
    public void delete(Long addressId) throws ResourceNotFoundException {
        Long userId = authUtils.getLoggedInUserId();

        Address address = addressRepo.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        addressRepo.delete(address);
    }
}