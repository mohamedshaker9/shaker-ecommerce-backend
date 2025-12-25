package com.shaker.shakerecommerce.service;


import com.shaker.shakerecommerce.dto.LoginUserDto;
import com.shaker.shakerecommerce.dto.RegisterUserDto;
import com.shaker.shakerecommerce.enums.AppRole;
import com.shaker.shakerecommerce.exceptions.APIException;
import com.shaker.shakerecommerce.model.Role;
import com.shaker.shakerecommerce.model.User;
import com.shaker.shakerecommerce.model.UserDetailsImpl;
import com.shaker.shakerecommerce.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;


    public AuthenticationService(
            UserRepo userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, ModelMapper modelMapper,
            RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User signup(RegisterUserDto registerUserDto) {
        if (userRepo.existsByEmail(registerUserDto.email())) {
            throw new APIException("User already exists with email");
        }

        if (userRepo.existsByUsername(registerUserDto.username())) {
            throw new APIException("User already exists with username");
        }

        //  User user = modelMapper.map(registerUserDto, User.class);
        User user = new User();
        user.setFullName(registerUserDto.fullName());
        user.setEmail(registerUserDto.email());
        user.setUsername(registerUserDto.username());
        user.setPassword(passwordEncoder.encode(registerUserDto.password()));

        Set<Role> roles = new HashSet<>();
        if(registerUserDto.isSeller()){
            roles.addAll(roleRepository.findAllByNameIn(List.of(AppRole.USER, AppRole.SELLER)));
        } else {
            roles.add(roleRepository.findByName(AppRole.USER));
        }

        user.setRoles(roles);
        return userRepo.save(user);
    }

    public UserDetails authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.email(),
                        loginUserDto.password()
                )
        );

        return userRepo.findByEmailOrUsername(loginUserDto.email(), loginUserDto.email()).map(UserDetailsImpl::new)
                .orElseThrow(() -> new APIException("Wrong email|userName or password"));
    }
}