package com.example.Mapping.Configuration;

import com.example.Mapping.JWT.JwtAuthFilter;
import com.example.Mapping.Repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
//    private final AppUserRepository userRepo;
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> userRepo.findByUsername(username)
//                .map(u -> User.withUsername(u.getUsername())
//                        .password(u.getPassword())
//                        .roles(u.getRoles().split(","))
//                        .build())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public
                        .requestMatchers("/auth/register", "/auth/login", "/auth/refresh").permitAll()

                        // ROLE_ADMIN required
                        .requestMatchers(HttpMethod.POST, "/api/save", "/api/save-full").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/{productCode}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/{productCode}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/{productCode}/add-tags").hasRole("ADMIN")

                        // ROLE_USER or ADMIN allowed
                        .requestMatchers(HttpMethod.POST, "/api/{productCode}/add-rating").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/{productCode}/add-feedbacks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")

                        // everything else needs authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
