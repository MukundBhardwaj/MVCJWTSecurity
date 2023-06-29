package com.mukund.mvcjwt.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mukund.mvcjwt.dto.AuthUserDTO;
import com.mukund.mvcjwt.service.AuthUserDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserRestController {

    private AuthUserDetailsService authUserDetailsService;

    public UserRestController(AuthUserDetailsService authUserDetailsService) {
        this.authUserDetailsService = authUserDetailsService;
    }

    @GetMapping
    public List<AuthUserDTO> getUsers() {
        return authUserDetailsService.loadAllUsers().parallelStream()
                .map(user -> new AuthUserDTO(user.getId(), user.getName()))
                .toList();
    }

    @PostMapping
    public AuthUserDTO createUser(@Valid @RequestBody AuthUserDTO authuserDTO) {
        return new AuthUserDTO(authUserDetailsService.createUser(authuserDTO));
    }

    @GetMapping("/{id}")
    public AuthUserDTO getUser(@PathVariable(name = "id", required = true) UUID id) {
        return new AuthUserDTO(authUserDetailsService.loadUserByID(id));
    }

    @PutMapping("/{id}")
    public AuthUserDTO updateUser(@PathVariable(name = "id", required = true) UUID id,
            @Valid @RequestBody AuthUserDTO authUserDTO) {
        return new AuthUserDTO(authUserDetailsService.updateUser(id, authUserDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "id", required = true) UUID id) {
        authUserDetailsService.deleteUser(id);
    }
}