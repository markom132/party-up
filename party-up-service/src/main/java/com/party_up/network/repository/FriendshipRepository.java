package com.party_up.network.repository;

import com.party_up.network.model.Friendship;
import com.party_up.network.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Friendship} entities.
 * Provides methods for performing CRUD operations and custom queries related to friendships.
 */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    /**
     * Returns a list of IDs of all friends for specific user.
     */
    @Query("SELECT CASE " +
            "WHEN f.userOne.id = :userId THEN f.userTwo.id " +
            "ELSE f.userOne.id END " +
            "FROM Friendship f " +
            "WHERE (f.userOne.id = :userId OR f.userTwo.id = :userId) " +
            "AND f.status = 'ACCEPTED'")
    List<Long> findFriendIdsByUserId(@Param("userId") Long userId);

    /**
     * Returns friendship object between two users.
     */
    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.userOne = :userOne AND f.userTwo = :userTwo) " +
            "   OR (f.userOne = :userTwo AND f.userTwo = :userOne)")
    Optional<Friendship> findFriendshipByUsers(@Param("userOne") User userOne, @Param("userTwo") User userTwo);

    /**
     * Check are two users friends or not.
     */
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Friendship f " +
            "WHERE ((f.userOne = :user1 AND f.userTwo = :user2) OR (f.userOne = :user2 AND f.userTwo = :user1)) " +
            "AND f.status = 'ACCEPTED'")
    boolean areUsersFriends(@Param("user1") User user1, @Param("user2") User user2);

    /**
     * Returns all PENDING request User IDs.
     */
    @Query("SELECT CASE " +
            "WHEN f.userOne.id = :userId THEN f.userTwo.id " +
            "ELSE f.userOne.id END " +
            "FROM Friendship f " +
            "WHERE (f.userOne.id = :userId OR f.userTwo.id = :userId) " +
            "AND f.status = 'PENDING'")
    List<Long> findPendingRequestsForUser(@Param("userId") Long userId);

    /**
     * Removes friendship connection between users.
     */
    @Modifying
    @Query("DELETE FROM Friendship f WHERE (f.userOne = :user1 AND f.userTwo = :user2) OR (f.userOne = :user2 AND f.userTwo = :user1)")
    void deleteFriendshipBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    /**
     * Return the number of user friends.
     */
    @Query("SELECT COUNT(f) " +
            "FROM Friendship f " +
            "WHERE (f.userOne = :user OR f.userTwo = :user) AND f.status = 'ACCEPTED'")
    int countFriends(@Param("user") User user);

    /**
     * Returns a list of all user friends(except the one who make this call).
     */
    @Query("SELECT CASE WHEN f.userOne = :otherUser THEN f.userTwo ELSE f.userOne END " +
            "FROM Friendship f " +
            "WHERE (f.userOne = :otherUser OR f.userTwo = :otherUser) " +
            "AND f.status = 'ACCEPTED' " +
            "AND (f.userOne != :currentUser AND f.userTwo != :currentUser)")
    List<User> findFriendsOfOtherUser(@Param("currentUser") User currentUser, @Param("otherUser") User otherUser);

    /**
     * Returns a list of mutual friends.
     */
    @Query("SELECT CASE WHEN f1.userOne = f2.userTwo THEN f1.userOne " +
            "           WHEN f1.userTwo = f2.userOne THEN f1.userTwo " +
            "           ELSE NULL END " +
            "FROM Friendship f1, Friendship f2 " +
            "WHERE (f1.userOne = :user1 OR f1.userTwo = :user1) " +
            "AND (f2.userOne = :user2 OR f2.userTwo = :user2) " +
            "AND f1.status = 'ACCEPTED' AND f2.status = 'ACCEPTED' " +
            "AND ((f1.userOne = f2.userTwo) OR (f1.userTwo = f2.userOne))")
    List<User> findMutualFriends(@Param("user1") User user1, @Param("user2") User user2);
}
