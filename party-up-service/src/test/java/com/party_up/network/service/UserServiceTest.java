package com.party_up.network.service;

import com.party_up.network.config.authentication.JwtUtil;
import com.party_up.network.exceptions.ResourceNotFoundException;
import com.party_up.network.model.AuthToken;
import com.party_up.network.model.User;
import com.party_up.network.model.dto.LoginRequestDTO;
import com.party_up.network.model.dto.LoginSuccessResponseDTO;
import com.party_up.network.model.enums.AccountStatus;
import com.party_up.network.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthTokenService authTokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    private AuthToken authToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword("$2a$10$abc123");
        user.setFirstName("JOHN");
        user.setLastName("Doe");
        user.setStatus(AccountStatus.ACTIVE);
        user.setUsername("username");

        authToken = new AuthToken();
        authToken.setToken("sampleToken");
        authToken.setExpiresAt(LocalDateTime.now().plusHours(1));
    }

   /* @Test
    void testFindUserUsernameSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByUsername("username");

        assertTrue(result.isPresent());
        assertEquals(user.getPassword(), result.get().getPassword());

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserByUsername("username");

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByUsername(anyString());
    }*/

    @Test
    public void testLogin_Success() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("username", "password");

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("generatedToken");
        when(authTokenService.createAuthToken(any(), any())).thenReturn(authToken);

        LoginSuccessResponseDTO response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals("generatedToken", response.getToken());
        assertNotNull(response.getExpiresAt());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("username", "wrongPassword");

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.login(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    public void testLogin_UserInactive() {
        user.setStatus(AccountStatus.INACTIVE);
        LoginRequestDTO loginRequest = new LoginRequestDTO("username", "password");

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.login(loginRequest));
        assertEquals("User account is inactive", exception.getMessage());
    }

    @Test
    public void testLogin_AuthenticationFailed() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("username", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Authentication failed") {
                });

        Exception exception = assertThrows(RuntimeException.class, () -> userService.login(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    public void testLogout_Success() {
        // Arrange: Mock an "authToken" cookie and return a valid token
        Cookie[] cookies = new Cookie[] {
                new Cookie("authToken", "sampleToken")
        };

        when(authTokenService.findByToken("sampleToken")).thenReturn(authToken);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getCookies()).thenReturn(cookies);

        // Act: Call the logout method
        userService.logout(String.valueOf(mockRequest));

        // Assert: Verify the token was marked as expired
        verify(authTokenService).updateToExpired(any());
    }

    @Test
    public void testLogout_TokenNotFound() {
        // Arrange: Mock an empty cookie or a missing token
        Cookie[] cookies = new Cookie[] {
                new Cookie("otherCookie", "otherValue") // No "authToken" cookie
        };

        when(authTokenService.findByToken(anyString()))
                .thenThrow(new ResourceNotFoundException("Token not found"));

        // Mock the HttpServletRequest to include the cookies
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getCookies()).thenReturn(cookies);

        // Act & Assert: Call the logout method and verify exception
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.logout(String.valueOf(mockRequest))
        );

        // Verify the exception message
        assertEquals("Token not found", exception.getMessage());
    }
}
