package com.party_up.network.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.party_up.network.model.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing an actual user.
 * This class is mapped to the 'users' table in the database.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username of the user.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * User password for logging in.
     */
    private String password;

    /**
     * Users first name.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * Users last name.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Email of the user(must be unique).
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * User account status (ACTIVE/INACTIVE).
     */
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    /**
     * User profile image.
     */
    @Lob
    private byte[] image;

    /**
     * Users day of birth.
     */
    private LocalDate birthDate;

    /**
     * Users age.
     */
    private int age;

    /**
     * User profile bio.
     */
    private String bio;

    /**
     * When user is created.
     */
    private LocalDateTime createdAt;

    /**
     * When user is last time updated.
     */
    private LocalDateTime lastUpdatedAt;

    /**
     * Users authTokens.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthToken> authTokens;

    /**
     * Constructor for creating a user with username, password, first name, last name, email, and status.
     *
     * @param username  the username of the user
     * @param password  the password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     * @param status    the status of the user
     */
    public User(String username, String password, String firstName,
                String lastName, String email, AccountStatus status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

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
