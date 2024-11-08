package com.party_up.network.config.authentication;

import com.party_up.network.model.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Mock
    private UserDetails userDetails;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();
    }

    @Test
    public void testGenerateToken() {
        when(user.getUsername()).thenReturn("testuser");
        String token = jwtUtil.generateToken(user);
        assertNotNull(token);
    }

    @Test
    public void testExtractUsername() {
        when(user.getUsername()).thenReturn("testuser");
        String token = jwtUtil.generateToken(user);
        String username = jwtUtil.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    public void testExtractExpiration() {
        when(user.getUsername()).thenReturn("testuser");
        String token = jwtUtil.generateToken(user);
        Date expiration = jwtUtil.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testValidateToken() {
        when(user.getUsername()).thenReturn("testuser");
        String token = jwtUtil.generateToken(user);
        when(userDetails.getUsername()).thenReturn("testuser");

        boolean isValid = jwtUtil.validateToken(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    public void testExtractClaim() {
        when(user.getUsername()).thenReturn("testuser");
        String token = jwtUtil.generateToken(user);

        Function<Claims, String> claimResolver = Claims::getSubject;
        String claim = jwtUtil.extractClaim(token, claimResolver);
        assertEquals("testuser", claim);
    }
}
