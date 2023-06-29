package com.mukund.mvcjwt.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mukund.mvcjwt.dto.AuthRequest;
import com.mukund.mvcjwt.dto.TokenDTO;
import com.mukund.mvcjwt.entity.AuthUser;
import com.mukund.mvcjwt.service.JWTService;
import com.mukund.mvcjwt.service.implementation.AuthUserDetailsServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticationRestController {

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailsServiceImpl authUserDetailsService;

    public AuthenticationRestController(JWTService jwtService, AuthenticationManager authenticationManager,
            AuthUserDetailsServiceImpl authUserDetailsService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.authUserDetailsService = authUserDetailsService;
    }

    @PostMapping("/token")
    public TokenDTO generateToken(@Valid @RequestBody AuthRequest authRequest) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        String accessToken = jwtService
                .generateToken((AuthUser) authUserDetailsService.loadUserByUsername(authRequest.username()));

        return new TokenDTO(accessToken, null);
    }

}