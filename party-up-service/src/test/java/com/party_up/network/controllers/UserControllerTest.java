package com.party_up.network.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.party_up.network.config.authentication.JwtUtil;
import com.party_up.network.model.User;
import com.party_up.network.model.dto.LoginRequestDTO;
import com.party_up.network.model.enums.AccountStatus;
import com.party_up.network.repository.RequestResponseLogRepository;
import com.party_up.network.repository.UserRepository;
import com.party_up.network.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RequestResponseLogRepository requestResponseLogRepository;

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequestDTO loginRequestDTO;

    private User mockUser;

    @BeforeEach
    void setUp() {
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("username");
        loginRequestDTO.setPassword("password123");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("username");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword(passwordEncoder.encode("password123"));
        mockUser.setStatus(AccountStatus.ACTIVE);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        when(userService.login(any(LoginRequestDTO.class))).thenReturn(createSuccessfulLoginResponse(mockUser));
        when(jwtUtil.validateToken(anyString(), any())).thenReturn(true);
        when(jwtUtil.extractUsername(anyString())).thenReturn("username");
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(mockUser));

        doNothing().when(userService).logout(any(String.class));
    }

    private Map<String, Object> createSuccessfulLoginResponse(User user) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Login successful");
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("status", String.valueOf(user.getStatus()));
        response.put("token", "mockedJwtToken");
        response.put("expiresAt", "2024-11-02 12:00:00");
        return response;
    }

    @Test
    void login_Success() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void login_Failure() throws Exception {
        when(userService.login(any(LoginRequestDTO.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error occurred during login request"))
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void logout_Successful() throws Exception {
        String jwtToken = "Bearer mockedJwtToken";

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));

        verify(userService).logout("Bearer mockedJwtToken");
    }

    @Test
    @WithMockUser(username = "testuser")
    void logout_MissingAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        mockMvc.perform(post("/api/auth/logout")
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("JWT token is missing or invalid"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void logout_ErrorDuringLogout() throws Exception {
        String jwtToken = "Bearer validToken";
        when(request.getHeader("Authorization")).thenReturn(jwtToken);

        doThrow(new RuntimeException("Logout failed")).when(userService).logout(anyString());

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Logout failed"));
    }

}
