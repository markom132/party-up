package com.party_up.network.dto.mappers;

import com.party_up.network.model.User;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.model.dto.mappers.UserMapper;
import com.party_up.network.model.enums.AccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    public void testToDTO_ValidEntity() {
        // Create a valid User entity
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setBio("Bio information");
        user.setStatus(AccountStatus.ACTIVE);
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setCreatedAt(LocalDateTime.now());
        user.setLastUpdatedAt(LocalDateTime.now());

        // Convert the User entity to UserDTO
        UserDTO userDTO = userMapper.toDTO(user);

        // Assert that the DTO is not null and values are correctly mapped
        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals("testUser", userDTO.getUsername());
        assertEquals("John", userDTO.getFirstName());
        assertEquals("Doe", userDTO.getLastName());
        assertEquals("john.doe@example.com", userDTO.getEmail());
        assertEquals("ACTIVE", userDTO.getStatus());
        assertEquals("1990-01-01", userDTO.getBirthDay());
        assertEquals("Bio information", userDTO.getBio());
        assertNotNull(userDTO.getCreatedAt());
        assertNotNull(userDTO.getLastUpdatedAt());
    }

    @Test
    public void testToDTO_NullEntity() {
        // Test when the entity is null
        UserDTO userDTO = userMapper.toDTO(null);

        // Assert that the result is null
        assertNull(userDTO);
    }

    @Test
    public void testToEntity_ValidDTO() {
        // Create a valid UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setBirthDay("1990-01-01");
        userDTO.setBio("Bio information");

        // Convert the UserDTO to User entity
        User user = userMapper.toEntity(userDTO);

        // Assert that the User entity is not null and values are correctly mapped
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(LocalDate.parse("1990-01-01"), user.getBirthDate());
        assertEquals(34, user.getAge());  // Assuming the test runs in 2024, age should be 34
        assertEquals("Bio information", user.getBio());
    }

    @Test
    public void testToEntity_NullDTO() {
        // Test when the DTO is null
        User user = userMapper.toEntity(null);

        // Assert that the result is null
        assertNull(user);
    }

    @Test
    public void testDateTimeFormatting() {
        // Create a UserDTO with a specific birth date
        String birthDate = "1990-01-01";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setBirthDay(birthDate);
        userDTO.setBio("Bio information");

        // Convert the UserDTO back to User entity
        User user = userMapper.toEntity(userDTO);

        // Assert that the birth date is properly parsed into LocalDate
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
    }
}
