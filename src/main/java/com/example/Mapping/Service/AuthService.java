package com.example.Mapping.Service;

import com.example.Mapping.JWT.JwtService;
import com.example.Mapping.RequestBean.*;
import com.example.Mapping.ResponceBean.AuthResponse;
import com.example.Mapping.Entity.AppUser;
import com.example.Mapping.Repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Register new user (no auto-login)
    public String register(RegisterRequest request) {
        if (userRepo.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("The username '" + request.username() + "' is already taken.");
        }

        AppUser user = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(request.role()) // directly from request
                .build();

        userRepo.save(user);
        return "User '" + request.username() + "' registered successfully with role: " + request.role();
    }



    // Login existing user -> generate both tokens
    public AuthResponse login(LoginRequest req) {
        try {
            var auth = new UsernamePasswordAuthenticationToken(req.username(), req.password());
            authManager.authenticate(auth);

            UserDetails ud = userDetailsService.loadUserByUsername(req.username());

            String accessToken = jwtService.generateAccessToken(ud);
            String refreshToken = jwtService.generateRefreshToken(ud);

            return new AuthResponse("Bearer", accessToken, refreshToken, ud.getUsername());

        } catch (Exception e) {
            throw new RuntimeException("Incorrect username or password. Please try again.");
        }
    }



    // Refresh -> only new access token
    public AuthResponse refresh(String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails ud = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(refreshToken, ud)) {
                throw new RuntimeException("Your refresh token is invalid or expired. Please login again.");
            }

            String newAccessToken = jwtService.generateAccessToken(ud);
            return new AuthResponse("Bearer", newAccessToken, refreshToken, username);

        } catch (Exception e) {
            throw new RuntimeException("Could not refresh token. Please login again.");
        }
    }
}
