package com.party_up.network.service;

import com.party_up.network.model.User;
import com.party_up.network.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("username");
        user.setPassword("password");
    }

    @Test
    void testFindUserUsernameSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByUsername("username");

        assertTrue(result.isPresent());
        assertEquals(user.getPassword(), result.get().getPassword());

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserByUsername("username");

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByUsername(anyString());
    }
}
