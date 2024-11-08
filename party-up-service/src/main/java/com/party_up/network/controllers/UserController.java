package com.party_up.network.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.party_up.network.model.dto.LoginRequestDTO;
import com.party_up.network.service.UserService;

/**
 * Controller for handling user-related operations such as authentication,
 * account activation, password management, and user information retrieval.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles user login requests.
     *
     * @param loginRequest the login request containing username and password
     * @return a response entity containing login response or error message
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = userService.login(loginRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            response.put("message", "Error occurred during login request");
            response.put("error", e.getMessage());
            logger.error("Login error for {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Handles user logout requests.
     *
     * @param request the HTTP servlet request
     * @return a response entity indicating the logout status
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.warn("Authorization header is missing or invalid during logout");
                return ResponseEntity.badRequest().body("Authorization header is missing or invalid");
            }

            userService.logout(authorizationHeader);
            logger.info("User logged out successfully");
            return ResponseEntity.ok("Logged out successfully");
        } catch (RuntimeException e) {
            logger.error("Logout error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
