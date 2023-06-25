package com.mukund.mvcjwt.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mukund.mvcjwt.dao.AuthUserRepository;
import com.mukund.mvcjwt.entity.AuthUser;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private AuthUserRepository authUserRepository;

    public AuthUserDetailsService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exist"));
    }

    public AuthUser loadUserByUUID(UUID uuid) throws UsernameNotFoundException {
        return authUserRepository.findById(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + uuid + " doesn't exist"));
    }

}