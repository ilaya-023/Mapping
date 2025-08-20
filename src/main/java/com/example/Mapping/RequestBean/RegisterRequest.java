package com.example.Mapping.RequestBean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        @Size(min = 3, max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
                message = "Username may contain letters, digits, dot, underscore, hyphen")
        String username,

        @NotBlank
        @Size(min = 6, max = 100)
        String password,

        @NotBlank
        String role // e.g. "USER" or "ADMIN"
) {}
