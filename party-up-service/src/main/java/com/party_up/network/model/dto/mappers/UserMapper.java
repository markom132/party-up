package com.party_up.network.model.dto.mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.party_up.network.model.User;
import com.party_up.network.model.dto.UserDTO;

/**
 * Mapper for converting between User entities and UserDTOs.
 */
@Slf4j
@Component
public class UserMapper {

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the User entity to convert
     * @return UserDTO with the User details, or null if the user is null
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            log.warn("Attempted to convert a null User to UserDTO");
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formatCreatedAt = user.getCreatedAt().format(formatter);
        String formatLastUpdatedAt = user.getLastUpdatedAt().format(formatter);
        String formatBirthDay = user.getBirthDate().format(format);

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus().name(),
                user.getImage(),
                formatBirthDay,
                user.getBio(),
                formatCreatedAt,
                formatLastUpdatedAt
        );
    }

    /**
     * Convert a list of User entities to a list of UserDTOs.
     *
     * @param users the list of User entities to convert
     * @return a list of UserDTOs
     */
    public List<UserDTO> toDtoList(List<User> users) {
        log.info("Converting list of User entities to list of UserDTOs. Total users: {}", users.size());
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert a UserDTO to a User entity.
     *
     * @param userDTO the UserDTO to convert
     * @return User entity, or null if UserDTO is null
     */
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            log.warn("Attempted to convert a null UserDTO to User");
            return null;
        }

        int age = Math.toIntExact(ChronoUnit.YEARS.between(LocalDate.parse(userDTO.getBirthDay()), LocalDate.now()));

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImage(userDTO.getImage());
        user.setBirthDate(LocalDate.parse(userDTO.getBirthDay()));
        user.setAge(age);
        user.setBio(userDTO.getBio());

        return user;
    }
}
