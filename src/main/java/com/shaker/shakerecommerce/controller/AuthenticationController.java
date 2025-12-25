package com.shaker.shakerecommerce.controller;

import com.shaker.shakerecommerce.dto.LoginResponse;
import com.shaker.shakerecommerce.dto.LoginUserDto;
import com.shaker.shakerecommerce.dto.RegisterUserDto;
import com.shaker.shakerecommerce.enums.AppRole;
import com.shaker.shakerecommerce.model.User;
import com.shaker.shakerecommerce.service.AuthenticationService;
import com.shaker.shakerecommerce.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {

            UserDetails authenticatedUser = authenticationService.authenticate(loginUserDto);

            String jwtToken = jwtService.generateToken(authenticatedUser);

            ResponseCookie jwtCookieToken = ResponseCookie.from("ecom-jwt-token", jwtToken )
                    .path("/api")
                    .maxAge(jwtService.getExpirationTime())
                    .httpOnly(false)
                    .secure(false)
                    .build();

            LoginResponse loginResponse = new LoginResponse(authenticatedUser.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)   // "ROLE_ADMIN"
                            .map(role -> role.replace("ROLE_", "")) // "ADMIN"
                            .map(AppRole::valueOf)                 // AppRole.ADMIN
                            .toList());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookieToken.toString())
                    .body(loginResponse);


    }
}