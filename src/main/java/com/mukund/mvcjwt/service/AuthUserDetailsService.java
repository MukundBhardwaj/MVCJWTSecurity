package com.mukund.mvcjwt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mukund.mvcjwt.dto.AuthUserDTO;
import com.mukund.mvcjwt.model.AuthUserDetails;

public interface AuthUserDetailsService extends UserDetailsService {

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public List<? extends AuthUserDetails> loadAllUsers();

    public AuthUserDetails loadUserByID(UUID id) throws UsernameNotFoundException;

    public AuthUserDetails createUser(AuthUserDTO user);

    public AuthUserDetails updateUser(UUID id, AuthUserDTO user) throws UsernameNotFoundException;

    public void deleteUser(UUID id) throws UsernameNotFoundException;
}