package com.party_up.network.service;

import com.party_up.network.exceptions.ResourceNotFoundException;
import com.party_up.network.model.Friendship;
import com.party_up.network.model.User;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.model.dto.mappers.UserMapper;
import com.party_up.network.model.enums.FriendshipStatus;
import com.party_up.network.repository.FriendshipRepository;
import com.party_up.network.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing friendship-related operations.
 */
@Slf4j
@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * Constructor for injecting the FriendshipRepository.
     *
     * @param friendshipRepository the repository for managing Friendship entities.
     */
    public FriendshipService(FriendshipRepository friendshipRepository, UserService userService, UserMapper userMapper, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    /**
     * Sends a friend request from one user to another.
     *
     * @param userOne the user sending the friend request.
     * @param userTwo the user receiving the friend request.
     * @return the created friendship object.
     */
    public Friendship sendFriendshipRequest(Long userOne, Long userTwo) {
        log.info("Sending friend request from user {} to user {}", userOne, userTwo);

        // Check are two IDs different
        if (userOne == userTwo) {
            log.warn("Friendship can't be created with yourself.");
            throw new IllegalArgumentException("Friendship can't be created with yourself");
        }

        // Search for users
        User sender = userService.getUserById(userOne);
        User recipient = userService.getUserById(userTwo);

        // Check if friendship already exist
        if (friendshipRepository.findFriendshipByUsers(sender, recipient).isPresent()) {
            log.warn("Friendship already exist between user {} and user {}", userOne, userTwo);
            throw new IllegalArgumentException("Friendship already exists between these users");
        }

        // Create new friendship
        Friendship friendship = new Friendship(sender, recipient, FriendshipStatus.PENDING);
        return friendshipRepository.save(friendship);
    }

    /**
     * Accepts a friend request.
     *
     * @param userOneId the ID of the first user in relationship.
     * @param userTwoId the ID of the second user in relationship.
     * @return the updated(accepted) Friendship object.
     */
    public Friendship acceptFriendRequest(Long userOneId, Long userTwoId) {
        // Find users
        User user1 = userService.getUserById(userOneId);
        User user2 = userService.getUserById(userTwoId);

        // Find friendship object between them
        Friendship friendship = friendshipRepository.findFriendshipByUsers(user1, user2)
                        .orElseThrow(() -> new ResourceNotFoundException("Friendship not found for users: " + userOneId + " and " + userTwoId));

        // Checking is friendship object in "PENDING" state
        if (!friendship.getStatus().equals(FriendshipStatus.PENDING)) {
            log.warn("Friendship is not in PENDING state, so can't be changed to accepted");
            throw new IllegalArgumentException("Friendship is not in PENDING state, so can't be accepted");
        }

        log.info("Accepting friend request with ID {}", friendship.getId());
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        return friendshipRepository.save(friendship);
    }

    /**
     * Rejects a friend request.
     *
     * @param userOneId the ID of the first user in the relationship.
     * @param userTwoId the ID of the second user in the relationship.
     */
    public void rejectFriendRequest(Long userOneId, Long userTwoId) {
        // Find users
        User userOne = userService.getUserById(userOneId);
        User userTwo = userService.getUserById(userTwoId);

        // Find friendship object between them
        Friendship friendship = friendshipRepository.findFriendshipByUsers(userOne, userTwo)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship not found for users: " + userOneId + " and " + userTwoId));

        // Checking is friendship object in "PENDING" state
        if (!friendship.getStatus().equals(FriendshipStatus.PENDING)) {
            log.warn("Friendship is not in PENDING state, so can't be changed to rejected");
            throw new IllegalArgumentException("Friendship is not in PENDING state, so can't be rejected");
        }

        log.info("Rejecting (deleting) friend request with ID {}", friendship.getId());
        friendshipRepository.delete(friendship);
    }

    /**
     * Gets all friends of a specific user.
     *
     * @param userId the user ID for whom to retrieve friends.
     * @return a list of User objects representing accepted friends.
     */
    public List<UserDTO> getFriends(Long userId) {
        // Find user IDs
        List<Long> ids = friendshipRepository.findFriendIdsByUserId(userId);

        // Get all friends based on their IDs
        List<User> users = userRepository.findAllById(ids);
        return userMapper.toDtoList(users);
    }


    /**
     * Checks if two users are friends.
     *
     * @param userOne the first user.
     * @param userTwo the second user.
     * @return true if the users are friends, false otherwise.
     */
    public boolean areUsersFriends(User userOne, User userTwo) {
        log.info("Checking if user {} and user {} are friends", userOne.getId(), userTwo.getId());
        return friendshipRepository.areUsersFriends(userOne, userTwo);
    }

    /**
     * Gets mutual friends between two users.
     *
     * @param userOne the first user.
     * @param userTwo the second user.
     * @return a list of mutual friends.
     */
    public List<User> getMutualFriends(User userOne, User userTwo) {
        log.info("Fetching mutual friends between user {} and user {}", userOne.getId(), userTwo.getId());
        return friendshipRepository.findMutualFriends(userOne, userTwo);
    }

    /**
     * Gets all friends of a specific user other than a given user.
     *
     * @param currentUser the user making the request.
     * @param otherUser the user whose friends are to be retrieved.
     * @return a list of friends for the specific user excluding the current user.
     */
    public List<User> getFriendsOfOtherUser(User currentUser, User otherUser) {
        log.info("Fetching friends of user {} excluding user {}", otherUser.getId(), currentUser.getId());
        return friendshipRepository.findFriendsOfOtherUser(currentUser, otherUser);
    }

    /**
     * Fetches a list of pending friend requests for a user.
     *
     * @param userId of the user.
     * @return list of pending friend requests as UserDTOs.
     */
    public List<UserDTO> getPendingFriendRequest(Long userId) {
        log.info("Fetching pending friend request for user with ID: {}", userId);
        // Find PENDING requests
        List<Long> userIDs = friendshipRepository.findPendingRequestsForUser(userId);
        List<User> pendingRequests = userRepository.findAllById(userIDs);

        log.info("Found {} pending friend requests for user with ID: {}", pendingRequests.size(), userId);
        return userMapper.toDtoList(pendingRequests);
    }

    /**
     * Deletes a friendship between two users.
     *
     * @param userOneId ID of the first user.
     * @param userTwoId ID of the second user.
     */
    public void deleteFriendship(Long userOneId, Long userTwoId) {
        log.info("Deleting friendship between user {} and user {}", userOneId, userTwoId);
        User userOne = userService.getUserById(userOneId);
        User userTwo = userService.getUserById(userTwoId);

        Optional<Friendship> friendship = friendshipRepository.findFriendshipByUsers(userOne, userTwo);

        if(friendship.isPresent()) {
            friendshipRepository.delete(friendship.get());
            log.info("Friendship successfully deleted between user {} and user {}", userOneId, userTwoId);
        } else {
            log.warn("No friendship found between user {} and user {}", userOneId, userTwoId);
            throw new ResourceNotFoundException("Friendship not found between the specified users.");
        }
    }
}
