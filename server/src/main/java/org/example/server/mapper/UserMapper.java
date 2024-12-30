package org.example.server.mapper;

import org.example.server.dto.request.UserDtoRequest;
import org.example.server.dto.response.UserDtoResponse;
import org.example.server.dto.response.UserSimplifiedDtoResponse;
import org.example.server.model.PositionEnum;
import org.example.server.model.User;
import org.example.server.model.UserProject;

import java.util.stream.Collectors;

public class UserMapper {
    public static User mapUserDtoRequestToUser(UserDtoRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPosition(PositionEnum.valueOf(request.getPosition()));
        return user;
    }

    public static UserDtoResponse mapUserToUserDtoResponse(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        UserDtoResponse response = new UserDtoResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPosition(String.valueOf(user.getPosition()));
        response.setCreatedDate(user.getCreatedDate());
        response.setUpdatedDate(user.getUpdatedDate());
        if(user.getCreatedProjects() != null) {
            response.setCreatedProjects(user.getCreatedProjects().stream()
                    .map(ProjectMapper::ProjectToProjectDtoResponse)
                    .collect(Collectors.toList()));
        }
        if (user.getUserProjects() != null) {
            response.setUserProjects(user.getUserProjects().stream()
                    .map(UserProjectMapper::mapUserProjectToUserProjectDtoResponse)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    public static UserSimplifiedDtoResponse toSimplifiedDto(User user) {
        return toSimplifiedDto(user, null);
    }

    public static UserSimplifiedDtoResponse toSimplifiedDto(UserProject userProject) {
        if (userProject == null || userProject.getUser() == null) {
            return null;
        }
        return toSimplifiedDto(userProject.getUser(), userProject.getRole().toString());
    }

    private static UserSimplifiedDtoResponse toSimplifiedDto(User user, String role) {
        if (user == null) {
            return null;
        }
        UserSimplifiedDtoResponse dto = new UserSimplifiedDtoResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPosition(user.getPosition() != null ? user.getPosition().toString() : null);
        dto.setRole(role);
        return dto;
    }




}
