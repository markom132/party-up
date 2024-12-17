package com.party_up.network.config.authentication;

import java.util.Collections;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.party_up.network.model.User;
import com.party_up.network.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class that implements UserDetailsService for Spring Security.
 * This service is responsible for loading user-specific data during authentication.
 */
@Slf4j
@Configuration
public class UserDetailsServiceConfig implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor for injecting the UserRepository dependency.
     *
     * @param userRepository the repository to interact with user data
     */
    public UserDetailsServiceConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by email for authentication.
     *
     * @param email the username of the user to be loaded
     * @return UserDetails containing user information
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(email)
                .orElseThrow(() -> {
                    log.warn("User not found with username: {}", email);
                    return new UsernameNotFoundException("User not found with username: " + email);
                });

        log.info("User found: {}", user.getUsername());

        // Returning the user details with an empty authority list
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList() // Here you can specify user authorities if needed
        );
    }
}
