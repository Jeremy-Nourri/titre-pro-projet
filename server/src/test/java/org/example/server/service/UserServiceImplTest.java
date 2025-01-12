package org.example.server.service;

import org.example.server.dto.request.UserDtoRequest;
import org.example.server.dto.response.UserDtoResponse;
import org.example.server.exception.EmailExistsException;
import org.example.server.exception.UserNotFoundException;
import org.example.server.mapper.SharedMapper;
import org.example.server.mapper.UserMapper;
import org.example.server.mapper.UserProjectMapper;
import org.example.server.model.PositionEnum;
import org.example.server.model.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private SharedMapper sharedMapper;

    @Mock
    private UserProjectMapper userProjectMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_success() {
        // Arrange
        UserDtoRequest request = new UserDtoRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPosition("DEVELOPER");

        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .position(PositionEnum.DEVELOPER)
                .build();

        UserDtoResponse responseDto = new UserDtoResponse();
        responseDto.setEmail("test@example.com");

        // On simule que l'email n'existe pas déjà :
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // On simule l'encodage du password :
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // On moque la conversion UserDtoRequest -> User :
        when(userMapper.mapUserDtoRequestToUser(any(UserDtoRequest.class))).thenReturn(user);

        // On moque la conversion User -> UserDtoResponse :
        when(userMapper.mapUserToUserDtoResponse(any(User.class))).thenReturn(responseDto);

        // On simule la sauvegarde en base :
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDtoResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).mapUserDtoRequestToUser(any(UserDtoRequest.class));
        verify(userMapper, times(1)).mapUserToUserDtoResponse(any(User.class));
    }

    @Test
    void createUser_emailExists_throwsException() {
        UserDtoRequest request = new UserDtoRequest();
        request.setEmail("test@example.com");

        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(existingUser));

        EmailExistsException exception = assertThrows(
                EmailExistsException.class,
                () -> userService.createUser(request)
        );

        assertEquals("Email déjà utilisé", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_success() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");

        UserDtoResponse dtoResponse = new UserDtoResponse();
        dtoResponse.setId(userId);
        dtoResponse.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.mapUserToUserDtoResponse(user)).thenReturn(dtoResponse);

        UserDtoResponse response = userService.getUserById(userId);

        assertNotNull(response, "Le résultat ne doit pas être nul");
        assertEquals(userId, response.getId(), "Les IDs doivent correspondre");
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).mapUserToUserDtoResponse(user);
    }

    @Test
    void getUserById_userNotFound_throwsException() {

        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(userId)
        );
        assertEquals("Utilisateur non trouvé avec l'id : " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
}
