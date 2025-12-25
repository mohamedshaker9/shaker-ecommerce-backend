package com.shaker.shakerecommerce.controller;


import com.shaker.shakerecommerce.dto.UserDto;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.User;
import com.shaker.shakerecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(userService.getUserByEmailOrUsername(currentUser.getUsername()));
    }

    @Transactional
    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers() {
        List <User> users = userService.getUsers();

        return ResponseEntity.ok(users);
    }
}