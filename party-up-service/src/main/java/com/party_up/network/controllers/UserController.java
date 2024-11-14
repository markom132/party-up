package com.party_up.network.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.party_up.network.model.dto.LoginRequestDTO;
import com.party_up.network.model.dto.LoginSuccessResponseDTO;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.service.UserService;

/**
 * Controller for handling user-related operations such as authentication,
 * account activation, password management, and user information retrieval.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Enables access from React app
@Validated
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
        LoginSuccessResponseDTO response;
        try {
            response = userService.login(loginRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error("Login error for {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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

    /**
     * Endpoint for creating a new user.
     * Validates and processes the user data sent in the request body, then creates a new user in the system.
     *
     * @param userDTO The data transfer object containing user details for creation.
     * @return ResponseEntity containing the created UserDTO and HTTP status 201 on success,
     * or an error message with status 500 on failure.
     */
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            logger.info("Received request to create user with username: {}", userDTO.getUsername());

            // Delegates user creation to the service layer
            UserDTO userCreated = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
        } catch (RuntimeException e) {
            // Logs the error with exception details for debugging
            logger.error("Error creating user with username: {}", userDTO.getUsername(), e);

            // Returns an error response with status 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
