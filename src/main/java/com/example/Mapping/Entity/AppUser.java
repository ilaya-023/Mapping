package com.example.Mapping.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "app_users", uniqueConstraints = @UniqueConstraint(name = "uk_username", columnNames = "username"))
public class AppUser {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 50)
    private String username; // unique

    @Column(nullable = false)
    private String password; // stored as BCrypt

    @Column(nullable = false, length = 100)
    private String roles; // e.g. "USER" or "ADMIN", comma separated if needed
}

