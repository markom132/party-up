package com.party_up.network.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.party_up.network.model.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Lob
    private byte[] image;

    private LocalDateTime birthDate;

    private int age;

    private String bio;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthToken> authTokens;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}
