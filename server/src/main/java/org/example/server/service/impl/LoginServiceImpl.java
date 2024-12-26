package org.example.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.request.LoginDtoRequest;
import org.example.server.dto.response.LoginDtoResponse;
import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.dto.response.UserProjectDtoResponse;
import org.example.server.exception.InvalidCredentialsException;
import org.example.server.model.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.LoginService;
import org.example.server.security.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginDtoResponse login(LoginDtoRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        List<ProjectDtoResponse> createdProjects = user.getCreatedProjects()
                .stream()
                .map(project -> ProjectDtoResponse.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .description(project.getDescription())
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
                        .createdDate(project.getCreatedDate())
                        .updatedDate(project.getUpdatedDate())
                        .build())
                .toList();

        List<UserProjectDtoResponse> userProjects = user.getUserProjects().stream()
                .map(userProject -> UserProjectDtoResponse.builder()
                        .id(userProject.getId())
                        .projectId(userProject.getProject().getId())
                        .projectName(userProject.getProject().getName())
                        .userAddedAt(userProject.getUserAddAt())
                        .build())
                .toList();

        return LoginDtoResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .position(String.valueOf(user.getPosition()))
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .token(jwtTokenUtil.generateToken(user.getEmail(), user.getId()))
                .createdProjects(createdProjects)
                .userProjects(userProjects)
                .build();
    }
}