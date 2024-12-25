package com.party_up.network.service;

import com.party_up.network.model.Friendship;
import com.party_up.network.model.User;
import com.party_up.network.model.dto.UserDTO;
import com.party_up.network.model.dto.mappers.UserMapper;
import com.party_up.network.model.enums.FriendshipStatus;
import com.party_up.network.repository.FriendshipRepository;
import com.party_up.network.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for FriendshipService
 */
class FriendshipServiceTest {

    @InjectMocks
    private FriendshipService friendshipService;

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User userOne;
    private User userTwo;
    private Friendship friendship;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userOne = new User();
        userOne.setId(1L);
        userOne.setUsername("UserOne");

        userTwo = new User();
        userTwo.setId(2L);
        userTwo.setUsername("UserTwo");

        friendship = new Friendship(userOne, userTwo, FriendshipStatus.PENDING);
        friendship.setId(3L);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(friendshipRepository, userService, userRepository, userMapper);
    }

    @Test
    void sendFriendshipRequest_Success() {
        when(userService.getUserById(1L)).thenReturn(userOne);
        when(userService.getUserById(2L)).thenReturn(userTwo);
        when(friendshipRepository.findFriendshipByUsers(userOne, userTwo)).thenReturn(Optional.empty());
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        Friendship result = friendshipService.sendFriendshipRequest(1L, 2L);

        assertNotNull(result);
        assertEquals(FriendshipStatus.PENDING, result.getStatus());
        verify(friendshipRepository).save(any(Friendship.class));
    }

    @Test
    void sendFriendshipRequest_AlreadyFriends() {
        when(userService.getUserById(1L)).thenReturn(userOne);
        when(userService.getUserById(2L)).thenReturn(userTwo);
        when(friendshipRepository.findFriendshipByUsers(userOne, userTwo))
                .thenReturn(Optional.of(friendship));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> friendshipService.sendFriendshipRequest(1L, 2L));

        assertEquals("Friendship already exists between these users", exception.getMessage());
    }

    @Test
    void sendFriendshipRequest_WithYourself() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> friendshipService.sendFriendshipRequest(1L, 1L));

        assertEquals("Friendship can't be created with yourself", exception.getMessage());
    }

    @Test
    void acceptFriendRequest_Success() {
        // Arrange
        friendship.setStatus(FriendshipStatus.PENDING);

        when(userService.getUserById(1L)).thenReturn(userOne);
        when(userService.getUserById(2L)).thenReturn(userTwo);
        when(friendshipRepository.findFriendshipByUsers(any(), any()))
                .thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(friendship))
                .thenReturn(friendship);

        // Act
        Friendship result = friendshipService.acceptFriendRequest(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(FriendshipStatus.ACCEPTED, result.getStatus());
        verify(friendshipRepository).save(friendship);
        verify(friendshipRepository).findFriendshipByUsers(userOne, userTwo);
    }

    @Test
    void acceptFriendRequest_NotPending() {
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        when(userService.getUserById(1L)).thenReturn(userOne);
        when(userService.getUserById(2L)).thenReturn(userTwo);
        when(friendshipRepository.findFriendshipByUsers(userOne, userTwo))
                .thenReturn(Optional.of(friendship));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> friendshipService.acceptFriendRequest(1L, 2L));

        assertEquals("Friendship is not in PENDING state, so can't be accepted", exception.getMessage());
    }

    @Test
    void rejectFriendRequest_Success() {
        friendship.setStatus(FriendshipStatus.PENDING);
        when(userService.getUserById(1L)).thenReturn(userOne);
        when(userService.getUserById(2L)).thenReturn(userTwo);
        when(friendshipRepository.findFriendshipByUsers(userOne, userTwo))
                .thenReturn(Optional.of(friendship));

        assertDoesNotThrow(() -> friendshipService.rejectFriendRequest(1L, 2L));
        verify(friendshipRepository).delete(friendship);
    }

    @Test
    void rejectFriendRequest_NotPending() {
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        when(userService.getUserById(1L)).thenReturn(userOne);
        when(userService.getUserById(2L)).thenReturn(userTwo);
        when(friendshipRepository.findFriendshipByUsers(userOne, userTwo))
                .thenReturn(Optional.of(friendship));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> friendshipService.rejectFriendRequest(1L, 2L));

        assertEquals("Friendship is not in PENDING state, so can't be rejected", exception.getMessage());
    }

    @Test
    void getFriends_Success() {
        List<Long> friendIds = List.of(2L);
        List<User> friends = List.of(userTwo);
        List<UserDTO> friendDTOs = List.of(new UserDTO());

        when(friendshipRepository.findFriendIdsByUserId(1L)).thenReturn(friendIds);
        when(userRepository.findAllById(friendIds)).thenReturn(friends);
        when(userMapper.toDtoList(friends)).thenReturn(friendDTOs);

        List<UserDTO> result = friendshipService.getFriends(1L);

        assertEquals(1, result.size());
    }
}