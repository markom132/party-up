package com.party_up.network.config.authentication;

import com.party_up.network.model.AuthToken;
import com.party_up.network.repository.AuthTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class JwtRequestFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthTokenRepository authTokenRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtRequestFilter = new JwtRequestFilter(userDetailsService, jwtUtil, authTokenRepository);
        responseWriter = new StringWriter();
    }

    @Test
    void testDoFilterInternalValidToken() throws Exception {
        String validToken = "valid-token";
        String username = "testuser";

        // Mocking cookies
        Cookie authCookie = new Cookie("authToken", validToken);
        when(request.getCookies()).thenReturn(new Cookie[]{authCookie});
        when(request.getRequestURI()).thenReturn("/api/some-secure-endpoint");

        // Mocking JWT validation and user details
        AuthToken authToken = new AuthToken();
        authToken.setToken(validToken);
        authToken.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        when(jwtUtil.extractUsername(validToken)).thenReturn(username);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(validToken, userDetails)).thenReturn(true);
        when(authTokenRepository.findByToken(validToken)).thenReturn(Optional.of(authToken));

        // Mocking HttpServletResponse writer
        PrintWriter writer = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(writer);

        // Execute filter
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verify behaviors
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(jwtUtil, times(1)).extractUsername(validToken);
        verify(jwtUtil, times(1)).validateToken(validToken, userDetails);
        verify(authTokenRepository, times(1)).findByToken(validToken);
    }

    @Test
    @WithMockUser
    void testDoFilterInternalExpiredToken() throws ServletException, IOException {
        String expiredToken = "expired-token";

        // Mocking cookies with an expired authToken
        Cookie expiredAuthCookie = new Cookie("authToken", expiredToken);
        when(request.getCookies()).thenReturn(new Cookie[]{expiredAuthCookie});
        when(request.getRequestURI()).thenReturn("/api/some-secure-endpoint");
        when(jwtUtil.extractUsername(expiredToken))
                .thenThrow(new ExpiredJwtException(null, null, "Expired JWT token"));

        // Mocking PrintWriter to avoid NullPointerException when getWriter() is called
        PrintWriter mockWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(mockWriter);

        // Execute the filter
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Assertions and verifications
        verify(filterChain, never()).doFilter(request, response); // Ensure filter chain is not called
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Ensure response status is set
        verify(mockWriter, times(1)).write("Expired JWT token"); // Ensure error message is written

        verify(userDetailsService, never()).loadUserByUsername(anyString()); // Ensure user details are not loaded
        verify(authTokenRepository, never()).findByToken(anyString()); // Ensure repository is not queried
    }

    @Test
    @WithMockUser
    void testDoFilterInternalNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/some-secure-endpoint");

        // Mocking PrintWriter to avoid NullPointerException when getWriter() is called
        PrintWriter mockWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(mockWriter);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(mockWriter, times(1)).write("JWT token is missing or invalid");
        verify(filterChain, never()).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}
