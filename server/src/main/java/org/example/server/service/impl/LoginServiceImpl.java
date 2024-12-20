package org.example.server.service.impl;

import org.example.server.dto.request.LoginDtoRequest;
import org.example.server.dto.response.LoginDtoResponse;
import org.example.server.exception.InvalidCredentialsException;
import org.example.server.model.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.LoginService;
import org.example.server.security.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public LoginDtoResponse login(LoginDtoRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formattedCreatedAt = user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null;
        String formattedUpdatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : null;

        return LoginDtoResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .position(String.valueOf(user.getPosition()))
                .createdAt(formattedCreatedAt)
                .updatedAt(formattedUpdatedAt)
                .token(jwtTokenUtil.generateToken(user.getEmail(), user.getId()))
                .build();
    }
}

