package com.party_up.network.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for handling login requests. Includes user email and password validation.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDTO {

    /**
     * User's username for login. Must be valid and within character limits.
     */
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters")
    private String username;

    /**
     * User's password for login. Must meet minimum length requirements.
     */
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
