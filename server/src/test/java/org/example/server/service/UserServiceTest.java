package org.example.server.service;

import org.example.server.dto.request.UserDtoRequest;
import org.example.server.dto.response.UserDtoResponse;
import org.example.server.mapper.UserMapper;
import org.example.server.model.PositionEnum;
import org.example.server.model.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {

        UserDtoRequest userDtoRequest = new UserDtoRequest();
        userDtoRequest.setFirstName("John");
        userDtoRequest.setLastName("Doe");
        userDtoRequest.setEmail("john@doe.com");
        userDtoRequest.setPassword("password");
        userDtoRequest.setPosition(String.valueOf(PositionEnum.DEVELOPER));

        User userToSave = UserMapper.mapUserDtoRequestToUser(userDtoRequest);
        String encodedPassword = "encoded_password";

        when(passwordEncoder.encode(userDtoRequest.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        UserDtoResponse response = userService.createUser(userDtoRequest);

        verify(passwordEncoder, times(1)).encode(userDtoRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john@doe.com", response.getEmail());
        assertEquals(PositionEnum.DEVELOPER.name(), response.getPosition());
    }
}
