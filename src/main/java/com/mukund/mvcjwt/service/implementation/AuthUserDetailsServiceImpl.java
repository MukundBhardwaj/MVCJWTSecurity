package com.mukund.mvcjwt.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mukund.mvcjwt.dao.AuthUserRepository;
import com.mukund.mvcjwt.dto.AuthUserDTO;
import com.mukund.mvcjwt.entity.AuthUser;
import com.mukund.mvcjwt.model.AuthUserDetails;
import com.mukund.mvcjwt.service.AuthUserDetailsService;

@Service
public class AuthUserDetailsServiceImpl implements AuthUserDetailsService {

    private AuthUserRepository authUserRepository;
    private PasswordEncoder passwordEncoder;

    public AuthUserDetailsServiceImpl(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exist"));
    }

    public AuthUser loadUserByID(UUID id) throws UsernameNotFoundException {
        return authUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " doesn't exist"));
    }

    @Override
    public List<? extends AuthUserDetails> loadAllUsers() {
        return authUserRepository.findAll();
    }

    @Override
    public AuthUserDetails createUser(AuthUserDTO user) {
        return authUserRepository
                .save(new AuthUser(user.name(), user.username(), user.password(), "ROLE_USER", user.enabled()));
    }

    @Override
    public AuthUserDetails updateUser(UUID id, AuthUserDTO user) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " doesn't exist"));
        authUser.setEnabled(user.enabled());
        authUser.setName(user.name());
        authUser.setPassword(passwordEncoder.encode(user.password()));
        return authUserRepository.save(authUser);
    }

    @Override
    public void deleteUser(UUID id) throws UsernameNotFoundException {
        if (authUserRepository.existsById(id))
            authUserRepository.deleteById(id);
        else
            throw new UsernameNotFoundException("User with id " + id + " doesn't exist");
    }

}