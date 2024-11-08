package com.party_up.network.service;

import com.party_up.network.config.authentication.JwtUtil;
import com.party_up.network.exceptions.DatabaseException;
import com.party_up.network.exceptions.ResourceNotFoundException;
import com.party_up.network.model.AuthToken;
import com.party_up.network.model.User;
import com.party_up.network.repository.AuthTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthTokenService {

    private final AuthTokenRepository authTokenRepository;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(AuthTokenService.class);

    public AuthTokenService(AuthTokenRepository authTokenRepository, JwtUtil jwtUtil) {
        this.authTokenRepository = authTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Creates a new authentication token in the database for the given user.
     *
     * @param token the JWT token
     * @param user  the user associated with the token
     * @return the created AuthToken
     * @throws DatabaseException if unable to create the auth token due to database error
     */
    public AuthToken createAuthToken(String token, User user) {
        try {
            AuthToken authToken = new AuthToken();
            authToken.setToken(token);
            authToken.setUser(user);
            authToken.setCreatedAt(LocalDateTime.now());
            authToken.setLastUsedAt(LocalDateTime.now());

            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);
            authToken.setExpiresAt(expiresAt);

            logger.info("Creating auth token for user: {}", user.getUsername());
            return authTokenRepository.save(authToken);
        } catch (DataAccessException e) {
            logger.error("Database error while creating auth token: {}", e.getMessage());
            throw new DatabaseException("Unable to create auth token " + e);
        }
    }

    /**
     * Marks the given authentication token as expired.
     *
     * @param authToken the token to be updated
     */
    public void updateToExpired(AuthToken authToken) {
        authToken.setExpiresAt(LocalDateTime.now());
        authTokenRepository.save(authToken);
        logger.info("Auth token for user {} marked as expired", authToken.getUser().getEmail());
    }

    /**
     * Finds an authentication token by its string representation.
     *
     * @param token the token string to search for
     * @return the found AuthToken
     * @throws ResourceNotFoundException if the token is not found
     */
    public AuthToken findByToken(String token) {
        try {
            return authTokenRepository.findByToken(token).orElseThrow(() ->
                    new ResourceNotFoundException("Token not found: " + token)
            );
        } catch (DataAccessException e) {
            logger.error("Resource not found: {},", e.getMessage());
            throw new ResourceNotFoundException("Token not found" + e);
        }
    }
}
