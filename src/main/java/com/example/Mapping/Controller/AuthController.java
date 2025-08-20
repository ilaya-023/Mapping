package com.example.Mapping.Controller;

import com.example.Mapping.JWT.JwtService;
import com.example.Mapping.Service.AuthService;
import com.example.Mapping.RequestBean.*;
import com.example.Mapping.ResponceBean.AuthResponse;
import com.example.Mapping.ResponceBean.WhoAmIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String message = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Oops! " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Something went wrong. Please try again later."));
        }
    }

    // Login -> get both access & refresh tokens
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Login failed! " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Something went wrong. Please try again later."));
        }
    }

    // Who Am I -> from access token
    @GetMapping("/whoami")
    public ResponseEntity<?> whoAmI(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            WhoAmIResponse response = new WhoAmIResponse(jwtService.extractUsername(token));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired session. Please login again."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Something went wrong. Please try again later."));
        }
    }

    // Refresh token
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");  // client sends { "refreshToken": "xxx" }
            AuthResponse newTokens = authService.refresh(refreshToken);

            return ResponseEntity.ok(Map.of(
                    "accessToken", newTokens.accessToken(),
                    "refreshToken", newTokens.refreshToken()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Your refresh token is invalid or expired. Please login again."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Something went wrong. Please try again later."));
        }
    }
}
