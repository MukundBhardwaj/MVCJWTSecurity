package com.mukund.mvcjwt.model;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthUserDetails extends UserDetails {

    public UUID getId();

    @Override
    public String getUsername();

    @Override
    public String getPassword();

    public String getName();

    public String getRole();

    @Override
    public boolean isEnabled();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities();

    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }

}