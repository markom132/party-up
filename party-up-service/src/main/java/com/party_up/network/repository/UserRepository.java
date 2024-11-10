package com.party_up.network.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.party_up.network.model.User;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods for performing CRUD operations and custom queries related to users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an {@link Optional} containing the found {@link User}, or empty if no user is found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an {@link Optional} containing the found {@link User}, or empty if no user is found
     */
    Optional<User> findByEmail(String email);
}
