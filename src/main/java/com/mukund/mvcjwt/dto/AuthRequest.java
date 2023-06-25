package com.mukund.mvcjwt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(@NotBlank @Size(min = 3, max = 30) @Email String username, @NotBlank String password) {
}