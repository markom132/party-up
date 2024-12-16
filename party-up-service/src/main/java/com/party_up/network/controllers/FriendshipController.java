package com.party_up.network.controllers;

import com.party_up.network.model.User;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.service.FriendshipService;
import com.party_up.network.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing friendship in the application.
 */
@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
@Slf4j
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserService userService;

    /**
     * Endpoint to send a friend request.
     *
     * @param senderId ID of the user sending the request.
     * @param recipientId ID of the user receiving the request.
     * @return Response entity with success message.
     */
    @PostMapping("/send-request")
    public ResponseEntity<String> sendFriendRequest(
            @RequestParam Long senderId,
            @RequestParam Long recipientId) {
        friendshipService.sendFriendshipRequest(senderId, recipientId);
        return ResponseEntity.status(HttpStatus.OK).body("Friend request sent successfully.");
    }

    /**
     * Endpoint to accept a friend request.
     *
     * @param userOneId ID of the first user in the relationship
     * @param userTwoId ID of the second user in the relationship
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/accept-request")
    public ResponseEntity<String> acceptFriendRequest(
            @RequestParam Long userOneId,
            @RequestParam Long userTwoId) {
        friendshipService.acceptFriendRequest(userOneId, userTwoId);
        return ResponseEntity.status(HttpStatus.OK).body("Friend request accepted successfully.");
    }

    /**
     * Endpoint to decline a friend request.
     *
     * @param userOneId ID of the user who sent the request.
     * @param userTwoId ID of the user declining the request.
     * @return ResponseEntity with success message.
     */
    @PostMapping("/decline-request")
    public ResponseEntity<String> declineFriendRequest(
            @RequestParam Long userOneId,
            @RequestParam Long userTwoId) {
        friendshipService.rejectFriendRequest(userOneId, userTwoId);
        return ResponseEntity.status(HttpStatus.OK).body("Friend request declined successfully.");
    }

    /**
     * Emdpoint to delete a friendship.
     *
     * @param userId ID of the first user.
     * @param friendId ID of the second user.
     * @return ResponseEntity with success message.
     */
    @PostMapping("/remove-friend")
    public ResponseEntity<String> removeFriend(
            @RequestParam Long userId,
            @RequestParam Long friendId) {
        friendshipService.deleteFriendship(userId, friendId);
        return ResponseEntity.status(HttpStatus.OK).body("Friendship removed successfully.");
    }

    /**
     * Endpoint to get all friends of a user.
     *
     * @param userId ID of the user.
     * @return List of friends (in UserDTOs).
     */
    @GetMapping("/friends")
    public ResponseEntity<List<UserDTO>> getFriends(@RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendshipService.getFriends(userId));
    }
}
