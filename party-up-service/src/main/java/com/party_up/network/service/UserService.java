package com.party_up.network.service;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.party_up.network.config.authentication.JwtUtil;
import com.party_up.network.model.AuthToken;
import com.party_up.network.model.User;
import com.party_up.network.model.dto.LoginRequestDTO;
import com.party_up.network.model.dto.LoginSuccessResponseDTO;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.model.dto.mappers.UserMapper;
import com.party_up.network.model.enums.AccountStatus;
import com.party_up.network.repository.UserRepository;

/**
 * Service class for managing user-related operations such as login, account activation,
 * password reset, and user data retrieval.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final AuthTokenService authTokenService;

    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                       AuthTokenService authTokenService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authTokenService = authTokenService;
        this.userMapper = userMapper;
    }

    /**
     * Authenticates a user using their email and password and generates a JWT token if successful.
     *
     * @param loginRequestDTO containing username and password for login
     * @return Map containing user details and JWT token
     */
    public LoginSuccessResponseDTO login(LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        try {
            // Fetch user by username
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!user.getStatus().equals(AccountStatus.ACTIVE)) {
                throw new RuntimeException("User account is inactive");
            }

            // Authenticate the user using AuthenticationManager
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Generate JWT token
            String token = jwtUtil.generateToken(user);

            AuthToken authToken = authTokenService.createAuthToken(token, user);

            // Format expiration date and prepare response
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedExpiresAt = authToken.getExpiresAt().format(formatter);

            LoginSuccessResponseDTO response = fillSuccessfulLoginResponse(user, token, formattedExpiresAt);

            logger.info("Login successful for user: {}", user.getEmail());
            return response;
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", username, e);
            throw new RuntimeException("Invalid credentials");
        }
    }

    /**
     * Logs out the user by marking the JWT token as expired.
     *
     * @param authorizationHeader authorization header containing JWT token
     */
    public void logout(String authorizationHeader) {
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ") ||
                authorizationHeader.length() < 8) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }
        logger.info("Logging out user with token: {}", authorizationHeader);
        String token = authorizationHeader.substring(7);
        AuthToken authToken = authTokenService.findByToken(token);

        authTokenService.updateToExpired(authToken);
        logger.info("Token {} marked as expired", token);
    }

    /**
     * Creates a new user based on the provided UserDTO.
     * Validates that the username and email are unique before creating the user.
     * Sets the user's status to INACTIVE upon creation.
     *
     * @param userDTO The data transfer object containing user information.
     * @return UserDTO representing the created user.
     * @throws RuntimeException if the username or email is already in use.
     */
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating new user with username: {}", userDTO.getUsername());

        // Extract username and email from the userDTO
        String username = userDTO.getUsername();
        String email = userDTO.getEmail();

        // Validate that the username and email are unique
        validateCreateUserRequest(username, email);

        // Map DTO to entity and set the status to INACTIVE
        User newUser = userMapper.toEntity(userDTO);
        newUser.setStatus(AccountStatus.INACTIVE);

        // Save the new user in the repository
        userRepository.save(newUser);
        logger.info("User successfully created with username: {}", userDTO.getUsername());

        // Convert entity back to DTO and return it
        return userMapper.toDTO(newUser);
    }

    /**
     * Validates that the provided username and email are unique.
     * If either the username or email is already in use, logs a warning and throws a RuntimeException.
     *
     * @param username The username to check for uniqueness.
     * @param email    The email to check for uniqueness.
     * @throws RuntimeException if the username or email is already in use.
     */
    public void validateCreateUserRequest(String username, String email) {
        // Check if a user with the given username or email already exists
        if (userRepository.findByUsername(username).isPresent()) {
            logger.warn("Validation failed: User already exists with username: {}", username);
            throw new RuntimeException("User already exists with username: " + username);
        } else if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("Validation failed: User already exists with email: {}", email);
            throw new RuntimeException("User already exists with email: " + email);
        }
    }

    /**
     * Populates a successful login response DTO with user details, a JWT token, and an expiration time.
     *
     * @param user      The authenticated user.
     * @param jwtToken  The JWT token generated for the user.
     * @param expiresAt The expiration time for the JWT token.
     * @return LoginSuccessResponseDTO containing user details, JWT token, and token expiration.
     */
    private LoginSuccessResponseDTO fillSuccessfulLoginResponse(User user, String jwtToken, String expiresAt) {
        logger.debug("Filling successful login response for user with ID: {}", user.getId());
        return new LoginSuccessResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                jwtToken,
                expiresAt
        );
    }

}
