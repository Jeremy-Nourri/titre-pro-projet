package org.example.server.service;

import org.example.server.model.User;
import org.example.server.dto.request.LoginDtoRequest;
import org.example.server.dto.response.LoginDtoResponse;
import org.example.server.repository.UserRepository;
import org.example.server.service.impl.LoginServiceImpl;
import org.example.server.security.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void testLoginSuccess() {
        LoginDtoRequest loginRequest = new LoginDtoRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(user.getEmail(), user.getId())).thenReturn("mockJwtToken");

        LoginDtoResponse response = loginService.login(loginRequest);

        assertNotNull(response);
        assertEquals("user@example.com", response.getEmail());
        assertEquals("mockJwtToken", response.getToken());
    }
}
