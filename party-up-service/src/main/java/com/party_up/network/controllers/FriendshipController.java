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

    @PostMapping("/send-request")
    public ResponseEntity<String> sendFriendRequest(
            @RequestParam Long senderId,
            @RequestParam Long recipientId) {
        friendshipService.sendFriendshipRequest(senderId, recipientId);
        return ResponseEntity.status(HttpStatus.OK).body("Friend request sent successfully.");
    }
}
