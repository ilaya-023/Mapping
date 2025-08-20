package com.example.Mapping.Service;

import com.example.Mapping.Repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .map(u -> {
                    UserBuilder builder = User.withUsername(u.getUsername())
                            .password(u.getPassword())
                            .roles(u.getRoles().split(",")); // split comma-separated roles
                    return builder.build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
