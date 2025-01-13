package org.example.server.service;

import io.jsonwebtoken.Claims;
import org.example.server.dto.request.LoginDtoRequest;
import org.example.server.dto.response.CreatedProjectsDtoResponse;
import org.example.server.dto.response.LoginDtoResponse;
import org.example.server.exception.InvalidCredentialsException;
import org.example.server.exception.TokenExpiredException;
import org.example.server.mapper.SharedMapper;
import org.example.server.mapper.UserProjectMapper;
import org.example.server.model.TokenBlacklist;
import org.example.server.model.User;
import org.example.server.model.Project;
import org.example.server.repository.TokenBlacklistRepository;
import org.example.server.repository.UserRepository;
import org.example.server.security.JwtTokenUtil;
import org.example.server.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Mock
    private SharedMapper sharedMapper;

    @Mock
    private UserProjectMapper userProjectMapper;

    @InjectMocks
    private LoginServiceImpl loginService;

    @Test
    void testLogin_Success() {

        LoginDtoRequest request = new LoginDtoRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setFirstName("John");
        user.setLastName("Doe");

        List<Project> createdProjects = List.of(
                Project.builder()
                        .id(1L)
                        .name("Project 1")
                        .description("Description 1")
                        .startDate(LocalDate.of(2023, 1, 1))
                        .endDate(LocalDate.of(2023, 12, 31))
                        .createdDate(LocalDate.of(2023, 1, 1))
                        .updatedDate(LocalDate.of(2023, 6, 1))
                        .build()
        );
        user.setCreatedProjects(createdProjects);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtTokenUtil.generateToken("test@example.com", 1L)).thenReturn("mockToken");

        when(sharedMapper.projectToCreatedProjectsDtoResponse(any(Project.class)))
                .thenAnswer(invocation -> {
                    Project project = invocation.getArgument(0);
                    CreatedProjectsDtoResponse dto = new CreatedProjectsDtoResponse();
                    dto.setId(project.getId());
                    dto.setName(project.getName());
                    dto.setDescription(project.getDescription());
                    dto.setStartDate(project.getStartDate());
                    dto.setEndDate(project.getEndDate());
                    dto.setCreatedDate(project.getCreatedDate());
                    dto.setUpdatedDate(project.getUpdatedDate());
                    return dto;
                });

        LoginDtoResponse response = loginService.login(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("mockToken", response.getToken());

        assertNotNull(response.getCreatedProjects());
        assertEquals(1, response.getCreatedProjects().size());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("password", "encodedPassword");
        verify(jwtTokenUtil).generateToken("test@example.com", 1L);
    }




    @Test
    void testLogin_InvalidEmail() {

        LoginDtoRequest request = new LoginDtoRequest();
        request.setEmail("invalid@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> loginService.login(request));
        verify(userRepository).findByEmail("invalid@example.com");
    }

    @Test
    void testLogin_InvalidPassword() {

        LoginDtoRequest request = new LoginDtoRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> loginService.login(request));
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
    }

    @Test
    void testLogout_Success() {

        String token = "validToken";
        Claims claims = mock(Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000)); // Future expiration
        when(jwtTokenUtil.validateToken(token)).thenReturn(claims);

        boolean result = loginService.logout(token);

        assertTrue(result);
        verify(jwtTokenUtil).validateToken(token);
        verify(tokenBlacklistRepository).save(any(TokenBlacklist.class));
    }

    @Test
    void testLogout_ExpiredToken() {

        String token = "expiredToken";
        when(jwtTokenUtil.validateToken(token)).thenThrow(new TokenExpiredException("Token expiré"));

        assertThrows(TokenExpiredException.class, () -> loginService.logout(token));
        verify(jwtTokenUtil).validateToken(token);
        verifyNoInteractions(tokenBlacklistRepository);
    }
}
