package com.mukund.mvcjwt.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.mukund.mvcjwt.model.AuthUserDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthUserDTO(
        @JsonProperty(access = Access.READ_ONLY) UUID id,
        @Email @NotBlank(message = "username cannot be blank") @Size(min = 3, max = 30) String username,
        @NotBlank(message = "name cannot be blank") @Size(min = 3, max = 30) String name,
        @NotNull(message = "enabled is required") Boolean enabled,
        @JsonProperty(access = Access.READ_ONLY) String role,
        @JsonProperty(access = Access.WRITE_ONLY) @NotBlank(message = "password cannot be blank") @Size(min = 3, max = 30, message = "password must be between 3-30 characters") String password) {
    @JsonCreator
    public AuthUserDTO(String username, String name, Boolean enabled, String password) {
        this(null, username, name, enabled, null, password);
    }

    public AuthUserDTO(UUID id, String name) {
        this(id, null, name, null, null, null);
    }

    public AuthUserDTO(AuthUserDetails authUser) {
        this(authUser.getId(), authUser.getUsername(), authUser.getName(), authUser.isEnabled(), authUser.getRole(),
                null);
    }
}