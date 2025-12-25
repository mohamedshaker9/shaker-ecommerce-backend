package com.shaker.shakerecommerce.service;


import com.shaker.shakerecommerce.dto.CategoryDTO;
import com.shaker.shakerecommerce.dto.CategoryResponse;
import com.shaker.shakerecommerce.dto.UserDto;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.Category;
import com.shaker.shakerecommerce.model.User;
import com.shaker.shakerecommerce.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {


    private final UserRepo userRepo;

    private final ModelMapper modelMapper;

    public UserService(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    public User getUserByEmailOrUsername(String emailOrUsername) throws ResourceNotFoundException {
        return userRepo.findByEmailOrUsername(emailOrUsername, emailOrUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", "emailOrUsername", emailOrUsername));

    }

    @Transactional
    public List<User> getUsers() {
        List<User> userList = userRepo.findAll();

         return new ArrayList<>(userList);


//        return userList.stream()
//                .map(user -> modelMapper.map(user, UserDto.class))
//                .toList();


    }

}
