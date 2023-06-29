package com.mukund.mvcjwt.entity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.mukund.mvcjwt.model.AuthUserDetails;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "auth_user")
public class AuthUser implements AuthUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(min = 3, max = 30)
    @Column(name = "name")
    private String name;

    @Email
    @Size(min = 3, max = 30)
    @Column(name = "email", unique = true)
    private String username;

    @Size(min = 3, max = 72)
    @Column(name = "password")
    private String password;

    @Size(max = 10)
    @Column(name = "role")
    private String role;

    @NonNull
    @Column(name = "active")
    private Boolean enabled;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String authorities) {
        this.role = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AuthUser(@Size(min = 3, max = 30) String name, @Email @Size(min = 3, max = 30) String username,
            @Size(min = 3, max = 72) String password, @Size(max = 10) String role, Boolean enabled) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    public AuthUser() {
    }

    @Override
    public String toString() {
        return "AuthUser [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password + ", role="
                + role + ", enabled=" + enabled + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AuthUser other = (AuthUser) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}