package com.party_up.network.controllers;

import com.party_up.network.service.FriendshipService;
import com.party_up.network.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
