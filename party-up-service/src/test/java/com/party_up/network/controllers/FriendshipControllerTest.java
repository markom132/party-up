package com.party_up.network.controllers;

import com.party_up.network.config.authentication.JwtUtil;
import com.party_up.network.exceptions.ResourceNotFoundException;
import com.party_up.network.model.Friendship;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.service.FriendshipService;
import com.party_up.network.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
public class FriendshipControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendshipService friendshipService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    /**
     * Creates a sample UserDTO object with predefined data.
     *
     * @return a fully populated UserDTO object
     */
    public static UserDTO createUserDTO(Long id, String username, String email) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUsername(username);
        userDTO.setFirstName("TestFirstName");
        userDTO.setLastName("TestLastName");
        userDTO.setEmail(email);
        userDTO.setStatus("ACTIVE");
        userDTO.setImage(new byte[]{});
        userDTO.setBirthDay("1990-01-01");
        userDTO.setBio("This is a test bio.");
        userDTO.setCreatedAt("2024-06-01T10:00:00");
        userDTO.setLastUpdatedAt("2024-06-02T12:00:00");
        return userDTO;
    }

    /**
     * Creates a list of sample UserDTO objects for testing.
     *
     * @return a list of UserDTO objects
     */
    public static List<UserDTO> createUserDTOList() {
        List<UserDTO> userList = new ArrayList<>();
        userList.add(createUserDTO(1L, "user1", "user1@example.com"));
        userList.add(createUserDTO(2L, "user2", "user2@example.com"));
        userList.add(createUserDTO(3L, "user3", "user3@example.com"));
        return userList;
    }

    /**
     * Creates a list of pending UserDTO objects for testing pending friend requests.
     *
     * @return a list of UserDTO objects with pending status
     */
    public static List<UserDTO> createPendingUserDTOList() {
        List<UserDTO> pendingList = new ArrayList<>();
        pendingList.add(createUserDTO(4L, "pendingUser1", "pending1@example.com"));
        pendingList.add(createUserDTO(5L, "pendingUser2", "pending2@example.com"));
        return pendingList;
    }

    /**
     * Creates a list of mutual UserDTO objects for testing mutual friends.
     *
     * @return a list of UserDTO objects representing mutual friends
     */
    public static List<UserDTO> createMutualUserDTOList() {
        List<UserDTO> mutualList = new ArrayList<>();
        mutualList.add(createUserDTO(6L, "mutualUser1", "mutual1@example.com"));
        mutualList.add(createUserDTO(7L, "mutualUser2", "mutual2@example.com"));
        return mutualList;
    }

    /**
     * Test for sending a friend request (Positive).
     */
    @Test
    @WithMockUser(username = "testuser")
    void sendFriendRequest_Success() throws Exception {
        // Arrange
        Friendship friendship = new Friendship();
        friendship.setId(1L);

        when(friendshipService.sendFriendshipRequest(1L, 2L)).thenReturn(friendship);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        // Act and Assert
        mockMvc.perform(post("/api/friendships/send-request")
                        .param("senderId", "1")
                        .param("recipientId", "2")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend request sent successfully."));
    }

    /**
     * Test for sending a friend request (Negative: Same user ID).
     */
    @Test
    @WithMockUser(username = "testuser")
    void sendFriendRequest_Failure_SameUserId() throws Exception {
        doThrow(new IllegalArgumentException("Friendship can't be created with yourself"))
                .when(friendshipService).sendFriendshipRequest(1L, 1L);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        mockMvc.perform(post("/api/friendships/send-request")
                        .param("senderId", "1")
                        .param("recipientId", "1")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Friendship can't be created with yourself"));
    }

    /**
     * Test for accepting a friend request (Positive).
     */
    @Test
    @WithMockUser(username = "testuser")
    void acceptFriendRequest_Success() throws Exception {
        Friendship friendship = new Friendship();
        friendship.setId(1L);

        when(friendshipService.acceptFriendRequest(1L, 2L)).thenReturn(friendship);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        mockMvc.perform(post("/api/friendships/accept-request")
                        .param("userOneId", "1")
                        .param("userTwoId", "2")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend request accepted successfully."));
    }

    /**
     * Test for declining a friend request (Positive).
     */
    @Test
    @WithMockUser(username = "testuser")
    void declineFriendRequest_Success() throws Exception {
        doNothing().when(friendshipService).rejectFriendRequest(1L, 2L);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        mockMvc.perform(post("/api/friendships/decline-request")
                        .param("userOneId", "1")
                        .param("userTwoId", "2")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend request declined successfully."));
    }

    /**
     * Test for removing a friend (Positive).
     */
    @Test
    @WithMockUser(username = "testuser")
    void removeFriend_Success() throws Exception {
        doNothing().when(friendshipService).deleteFriendship(1L, 2L);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        mockMvc.perform(post("/api/friendships/remove-friend")
                        .param("userId", "1")
                        .param("friendId", "2")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Friendship removed successfully."));
    }

    /**
     * Test for fetching all friends (Positive).
     */
    @Test
    @WithMockUser(username = "testuser")
    void getFriends_Success() throws Exception {
        List<UserDTO> friends = createUserDTOList();
        when(friendshipService.getFriends(anyLong())).thenReturn(friends);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        mockMvc.perform(get("/api/friendships/friends")
                        .param("userId", "1")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user1"));
    }

    /**
     * Test for fetching pending friend requests (Positive).
     */
    @Test
    @WithMockUser(username = "testuser")
    void getPendingRequests_Success() throws Exception {
        List<UserDTO> pendingRequests = createPendingUserDTOList();
        when(friendshipService.getPendingFriendRequest(anyLong())).thenReturn(pendingRequests);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        mockMvc.perform(get("/api/friendships/pending-requests")
                        .param("userId", "1")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[0].username").value("pendingUser1"));
    }


    /**
     * Test for fetching mutual friends (Positive).
     */
    @Test
    @WithMockUser(username = "testuser")
    void getMutualFriends_Success() throws Exception {
        List<UserDTO> mutualFriends = createMutualUserDTOList();
        when(friendshipService.getMutualFriends(1L, 2L)).thenReturn(mutualFriends);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        mockMvc.perform(get("/api/friendships/mutual-friends")
                        .param("userOneId", "1")
                        .param("userTwoId", "2")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(6))
                .andExpect(jsonPath("$[0].username").value("mutualUser1"));
    }

    /**
     * Test for exception handling (ResourceNotFoundException).
     */
    @Test
    @WithMockUser(username = "testuser")
    void acceptFriendRequest_ResourceNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Friendship not found"))
                .when(friendshipService).acceptFriendRequest(1L, 2L);

        MockCookie authTokenCookie = new MockCookie("authToken", "valid.jwt.token");
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false);
        authTokenCookie.setPath("/");

        mockMvc.perform(post("/api/friendships/accept-request")
                        .param("userOneId", "1")
                        .param("userTwoId", "2")
                        .cookie(authTokenCookie)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Friendship not found"));
    }
}
