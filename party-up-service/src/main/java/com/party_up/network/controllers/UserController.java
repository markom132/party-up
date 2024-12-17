package com.party_up.network.controllers;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.party_up.network.model.dto.LoginRequestDTO;
import com.party_up.network.model.dto.LoginSuccessResponseDTO;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling user-related operations such as authentication,
 * account activation, password management, and user information retrieval.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Enables access from React app
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles user login requests.
     *
     * @param loginRequest the login request containing username and password
     * @param response the Http Servlet response object
     * @return a response entity containing login response or error message
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {
        LoginSuccessResponseDTO responseDTO;
        try {
            responseDTO = userService.login(loginRequest);

            // Create Cookie for storing jwt token
            Cookie cookie = new Cookie("authToken", responseDTO.getToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Ensure this is true in production (requires HTTPS);
            cookie.setPath("/"); // Cookie valid for the entire domain
            cookie.setMaxAge(24 * 60 * 60); // 1 day expiration
            cookie.setDomain("localhost"); // Explicitly set the domain for localhost
            response.addCookie(cookie);

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (RuntimeException e) {
            log.error("Login error for {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Handles user logout requests.
     *
     * @param request the HTTP servlet request
     * @param response the Http Servlet response object
     * @return a response entity indicating the logout status
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Read token from cookies
            String token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "authToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (token == null) {
                log.warn("Authorization cookie is missing or invalid during logout");
                return ResponseEntity.badRequest().body("Authorization cookie is missing or invalid");
            }

            // Invalidate the token using the service
            userService.logout(token);

            // Remove the authToken cookie
            Cookie cookie = new Cookie("authToken", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(0); // Immediately expire
            response.addCookie(cookie);

            log.info("User logged out successfully");
            return ResponseEntity.ok("Logged out successfully");
        } catch (RuntimeException e) {
            log.error("Logout error: {}", e.getMessage());
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
            log.info("Received request to create user with username: {}", userDTO.getUsername());

            // Delegates user creation to the service layer
            UserDTO userCreated = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
        } catch (RuntimeException e) {
            // Logs the error with exception details for debugging
            log.error("Error creating user with username: {}", userDTO.getUsername(), e);

            // Returns an error response with status 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
