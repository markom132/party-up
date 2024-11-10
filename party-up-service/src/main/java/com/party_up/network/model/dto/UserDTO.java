package com.party_up.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing user information.
 * This class is used to transfer user data between different layers of the application.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String status;

    private byte[] image;

    private String birthDay;

    private int age;

    private String bio;

    private String createdAt;

    private String lastUpdatedAt;

}
