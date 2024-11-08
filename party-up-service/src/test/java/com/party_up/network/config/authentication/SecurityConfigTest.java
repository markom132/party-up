package com.party_up.network.config.authentication;

import com.party_up.network.model.User;
import com.party_up.network.repository.UserRepository;
import com.party_up.network.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPasswordEncoder() {
        // Test that the PasswordEncoder bean is a BCryptPasswordEncoder instance
        assertTrue(securityConfig.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    void testAuthenticationManager() throws Exception {
        // Mock AuthenticationConfiguration to return an AuthenticationManager
        AuthenticationConfiguration authenticationConfiguration = mock(AuthenticationConfiguration.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        // Test that the AuthenticationManager bean is created
        assertEquals(authenticationManager, securityConfig.authentication(authenticationConfiguration));
    }
}
