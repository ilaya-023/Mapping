package com.example.Mapping.ResponceBean;
public record AuthResponse(
        String tokenType,    // "Bearer"
        String accessToken,  // raw JWT
        String refreshToken, // refresh token
        String username      // subject
) {}

