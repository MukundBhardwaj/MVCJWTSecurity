package com.mukund.mvcjwt.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mukund.mvcjwt.dao.AuthUserRepository;
import com.mukund.mvcjwt.dto.AuthUserDTO;
import com.mukund.mvcjwt.entity.AuthUser;
import com.mukund.mvcjwt.exceptionhandler.ResourceNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserRestController {

    private AuthUserRepository authUserRepository;
    private PasswordEncoder passwordEncoder;

    public UserRestController(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<AuthUserDTO> getUsers() {
        return authUserRepository.findAllUsers();
    }

    @PostMapping
    public AuthUserDTO createUser(@Valid @RequestBody AuthUserDTO authuserDTO) {
        return new AuthUserDTO(authUserRepository
                .save(new AuthUser(null, authuserDTO.name(), authuserDTO.username(),
                        passwordEncoder.encode(authuserDTO.password()),
                        "ROLE_USER",
                        authuserDTO.enabled())));
    }

    @GetMapping("/{id}")
    public AuthUserDTO getUser(@PathVariable(name = "id", required = true) UUID id) {
        return new AuthUserDTO(authUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID - " + id + " not found")));
    }

    @PutMapping("/{id}")
    public AuthUserDTO updateUser(@PathVariable(name = "id", required = true) UUID id,
            @Valid @RequestBody AuthUserDTO authUserDTO) {
        AuthUser authUser = authUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID - " + id + " not found"));
        authUser.setName(authUserDTO.name());
        authUser.setPassword(authUserDTO.password());
        authUser.setEnabled(authUserDTO.enabled());
        return new AuthUserDTO(authUserRepository.save(authUser));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "id", required = true) UUID id) {
        if (authUserRepository.existsById(id))
            authUserRepository.deleteById(id);
        else
            throw new ResourceNotFoundException("User with ID - " + id + " not found");
    }
}
