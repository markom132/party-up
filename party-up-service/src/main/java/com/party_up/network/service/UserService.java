package com.party_up.network.service;

import java.time.format.DateTimeFormatter;
import java.util.*;

import com.party_up.network.config.authentication.JwtUtil;
import com.party_up.network.model.AuthToken;
import com.party_up.network.model.dto.LoginRequestDTO;
import com.party_up.network.model.enums.AccountStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.party_up.network.model.User;
import com.party_up.network.repository.UserRepository;

/**
 * Service class for managing user-related operations such as login, account activation,
 * password reset, and user data retrieval.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, AuthTokenService authTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authTokenService = authTokenService;
    }

    /**
     * Authenticates a user using their email and password and generates a JWT token if successful.
     *
     * @param loginRequestDTO containing username and password for login
     * @return Map containing user details and JWT token
     */
    public Map<String, Object> login(LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        try {
            // Fetch user by username
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!user.getStatus().equals(AccountStatus.ACTIVE)) {
                throw new RuntimeException("User account is inactive");
            }

            // Authenticate the user using AuthenticationManager
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Generate JWT token
            String token = jwtUtil.generateToken(user);

            AuthToken authToken = authTokenService.createAuthToken(token, user);

            // Format expiration date and prepare response
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedExpiresAt = authToken.getExpiresAt().format(formatter);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("status", String.valueOf(user.getStatus()));
            response.put("token", token);
            response.put("expiresAt", formattedExpiresAt);

            logger.info("Login successful for user: {}", user.getEmail());
            return response;
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for email: {}", username, e);
            throw new RuntimeException("Invalid credentials");
        }
    }

    /**
     * Logs out the user by marking the JWT token as expired.
     *
     * @param authorizationHeader authorization header containing JWT token
     */
    public void logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || authorizationHeader.length() < 8) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }
        logger.info("Logging out user with token: {}", authorizationHeader);
        String token = authorizationHeader.substring(7);
        AuthToken authToken = authTokenService.findByToken(token);

        authTokenService.updateToExpired(authToken);
        logger.info("Token {} marked as expired", token);
    }

    public Optional<User> findUserByUsername(String username) {
        logger.info("Fetching user with username: {}", username);
        return userRepository.findByUsername(username);
    }

}
