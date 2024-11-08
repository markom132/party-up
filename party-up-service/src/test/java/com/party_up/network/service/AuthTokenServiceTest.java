package com.party_up.network.service;

import com.party_up.network.config.authentication.JwtUtil;
import com.party_up.network.exceptions.DatabaseException;
import com.party_up.network.exceptions.ResourceNotFoundException;
import com.party_up.network.model.AuthToken;
import com.party_up.network.model.User;
import com.party_up.network.repository.AuthTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AuthTokenServiceTest {

    @Mock
    private AuthTokenRepository authTokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    private AuthTokenService authTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authTokenService = new AuthTokenService(authTokenRepository, jwtUtil);
    }


    @Test
    void createAuthToken_Success() {
        String token = "sampleToken123";
        User user = new User();
        user.setEmail("test@example.com");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(30);

        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setUser(user);
        authToken.setCreatedAt(now);
        authToken.setLastUsedAt(now);
        authToken.setExpiresAt(expiresAt);

        when(authTokenRepository.save(any(AuthToken.class))).thenReturn(authToken);

        AuthToken createdAuthToken = authTokenService.createAuthToken(token, user);

        assertNotNull(createdAuthToken, "AuthToken should not be null after save");
        assertEquals(token, createdAuthToken.getToken(), "Token should match input token");
        assertEquals(user, createdAuthToken.getUser(), "User should match input user");
        assertEquals(expiresAt.getMinute(), createdAuthToken.getExpiresAt().getMinute(), "Expires at time should be 30 minutes later");

        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    void createAuthToken_DatabaseException() {
        String token = "sampleToken123";
        User user = new User();
        user.setEmail("test@example.com");

        when(authTokenRepository.save(any(AuthToken.class))).thenThrow(new DataAccessException("Database error") {
        });

        DatabaseException exception = assertThrows(DatabaseException.class, () -> authTokenService.createAuthToken(token, user));
        assertTrue(exception.getMessage().contains("Unable to create auth token"));

        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    void updateToExpired_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        AuthToken authToken = new AuthToken();
        authToken.setUser(user);
        authToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        when(authTokenRepository.save(any(AuthToken.class))).thenReturn(authToken);

        authTokenService.updateToExpired(authToken);

        assertNotNull(authToken.getExpiresAt());
        assertEquals(LocalDateTime.now().getMinute(), authToken.getExpiresAt().getMinute());

        verify(authTokenRepository, times(1)).save(authToken);
    }

    @Test
    void updateToExpired_NullAuthToken() {
        assertThrows(NullPointerException.class, () -> authTokenService.updateToExpired(null));
    }

    @Test
    void updateToExpired_DatabaseException() {
        User user = new User();
        user.setEmail("test@example.com");

        AuthToken authToken = new AuthToken();
        authToken.setUser(user);

        when(authTokenRepository.save(any(AuthToken.class))).thenThrow(new DataAccessException("Database error") {
        });

        assertThrows(DataAccessException.class, () -> authTokenService.updateToExpired(authToken));

        verify(authTokenRepository, times(1)).save(authToken);
    }

    @Test
    void findByToken_Success() {
        String token = "sampleToken123";
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);

        when(authTokenRepository.findByToken(token)).thenReturn(Optional.of(authToken));

        AuthToken foundAuthToken = authTokenService.findByToken(token);

        assertNotNull(foundAuthToken);
        assertEquals(token, foundAuthToken.getToken());

        verify(authTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void findByToken_TokenNotFound() {
        String token = "nonExistentToken";

        when(authTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> authTokenService.findByToken(token));
        assertTrue(exception.getMessage().contains("Token not found: " + token));

        verify(authTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void findByToken_DatabaseException() {
        String token = "sampleToken123";

        when(authTokenRepository.findByToken(anyString())).thenThrow(new DataAccessException("Database error") {
        });

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> authTokenService.findByToken(token));
        assertTrue(exception.getMessage().contains("Database error"));

        verify(authTokenRepository, times(1)).findByToken(token);
    }

}
