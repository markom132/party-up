package com.party_up.network.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.party_up.network.model.Friendship;
import com.party_up.network.model.User;

/**
 * Repository interface for managing {@link Friendship} entities.
 * Provides methods for performing CRUD operations and custom queries related to friendships.
 */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    /**
     * @param userId ID of the user
     * @return Returns a list of IDs of all friends for specific user.
     */
    @Query("SELECT CASE " +
            "WHEN f.userOne.id = :userId THEN f.userTwo.id " +
            "ELSE f.userOne.id END " +
            "FROM Friendship f " +
            "WHERE (f.userOne.id = :userId OR f.userTwo.id = :userId) " +
            "AND f.status = 'ACCEPTED'")
    List<Long> findFriendIdsByUserId(@Param("userId") Long userId);

    /**
     * @param userOne first user.
     * @param userTwo second user.
     * @return Returns friendship object between two users.
     */
    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.userOne = :userOne AND f.userTwo = :userTwo) " +
            "   OR (f.userOne = :userTwo AND f.userTwo = :userOne)")
    Optional<Friendship> findFriendshipByUsers(@Param("userOne") User userOne, @Param("userTwo") User userTwo);

    /**
     * Check are two users friends or not.
     * @param user1 first user.
     * @param user2 second user.
     * @return boolean which represent are user friends or not.
     */
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Friendship f " +
            "WHERE ((f.userOne = :user1 AND f.userTwo = :user2) OR (f.userOne = :user2 AND f.userTwo = :user1)) " +
            "AND f.status = 'ACCEPTED'")
    boolean areUsersFriends(@Param("user1") User user1, @Param("user2") User user2);

    /**
     * @param userId ID of the user
     * @return Returns all PENDING request User IDs.
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
     * @param user1 first user.
     * @param user2 second user.
     */
    @Modifying
    @Query("DELETE FROM Friendship f WHERE (f.userOne = :user1 AND f.userTwo = :user2) " +
            "OR (f.userOne = :user2 AND f.userTwo = :user1)")
    void deleteFriendshipBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    /**
     * @param currentUser the user who make the call.
     * @param otherUser the user which friends we will retrieve.
     * @return Returns a list of all user friends(except the one who make this call).
     */
    @Query("SELECT CASE WHEN f.userOne = :otherUser THEN f.userTwo ELSE f.userOne END " +
            "FROM Friendship f " +
            "WHERE (f.userOne = :otherUser OR f.userTwo = :otherUser) " +
            "AND f.status = 'ACCEPTED' " +
            "AND (f.userOne != :currentUser AND f.userTwo != :currentUser)")
    List<User> findFriendsOfOtherUser(@Param("currentUser") User currentUser, @Param("otherUser") User otherUser);

    /**
     * @param user1 first user.
     * @param user2 second user.
     * @return Returns a list of mutual friends IDs.
     */
    @Query("SELECT DISTINCT CASE " +
            "WHEN f1.userOne.id = f2.userOne.id THEN f1.userOne.id " +
            "WHEN f1.userOne.id = f2.userTwo.id THEN f1.userOne.id " +
            "WHEN f1.userTwo.id = f2.userOne.id THEN f1.userTwo.id " +
            "WHEN f1.userTwo.id = f2.userTwo.id THEN f1.userTwo.id " +
            "ELSE NULL END " +
            "FROM Friendship f1, Friendship f2 " +
            "WHERE (f1.userOne.id = :user1 OR f1.userTwo.id = :user1) " +
            "AND (f2.userOne.id = :user2 OR f2.userTwo.id = :user2) " +
            "AND f1.status = 'ACCEPTED' " +
            "AND f2.status = 'ACCEPTED'")
    List<Long> findMutualFriendIds(@Param("user1") Long user1, @Param("user2") Long user2);
}
