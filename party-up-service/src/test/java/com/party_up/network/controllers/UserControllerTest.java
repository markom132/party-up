package com.party_up.network.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.party_up.network.config.authentication.JwtUtil;
import com.party_up.network.model.User;
import com.party_up.network.model.dto.LoginRequestDTO;
import com.party_up.network.model.dto.LoginSuccessResponseDTO;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.model.enums.AccountStatus;
import com.party_up.network.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


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
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequestDTO loginRequestDTO;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("username");
        loginRequestDTO.setPassword("password123");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("username");
        mockUser.setEmail("test@example.com");
        mockUser.setStatus(AccountStatus.ACTIVE);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setFirstName("Jane");
        userDTO.setLastName("Doe");
        userDTO.setEmail("jane.doe@example.com");
        userDTO.setBirthDay("1990-01-01");
        userDTO.setBio("A new user bio");

        when(userService.login(any(LoginRequestDTO.class))).thenReturn(createSuccessfulLoginResponse(mockUser));
        when(jwtUtil.validateToken(anyString(), any())).thenReturn(true);

        doNothing().when(userService).logout(any(String.class));
    }

    private LoginSuccessResponseDTO createSuccessfulLoginResponse(User user) {
        return new LoginSuccessResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                "mockedJwtToken",
                "2024-11-02 12:00:00"
        );
    }

    @Test
    void login_Success() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").value("mockedJwtToken"));
    }

    @Test
    void login_Failure() throws Exception {
        when(userService.login(any(LoginRequestDTO.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Invalid credentials"));
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
        mockMvc.perform(post("/api/auth/logout")
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("JWT token is missing or invalid"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void logout_ErrorDuringLogout() throws Exception {
        String jwtToken = "Bearer validToken";
        doThrow(new RuntimeException("Logout failed")).when(userService).logout(anyString());

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Logout failed"));
    }

    @Test
    void createUser_Success() throws Exception {
        // Mock UserDTO response after successful creation
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.birthDay").value("1990-01-01"))
                .andExpect(jsonPath("$.bio").value("A new user bio"));
    }

    @Test
    void createUser_Failure() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenThrow(new RuntimeException("Error creating user"));

        mockMvc.perform(post("/api/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error creating user"));
    }
}
