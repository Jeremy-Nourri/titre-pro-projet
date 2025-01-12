package org.example.server.mapper;

import org.example.server.dto.request.UserDtoRequest;
import org.example.server.dto.response.UserDtoResponse;
import org.example.server.dto.response.UserSimplifiedDtoResponse;
import org.example.server.model.PositionEnum;
import org.example.server.model.User;
import org.springframework.stereotype.Component;
import java.util.Objects;


import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final SharedMapper sharedMapper;
    private final UserProjectMapper userProjectMapper;

    public UserMapper(SharedMapper sharedMapper, UserProjectMapper userProjectMapper) {
        this.sharedMapper = sharedMapper;
        this.userProjectMapper = userProjectMapper;
    }

    public User mapUserDtoRequestToUser(UserDtoRequest request) {
        System.out.println("Mapping UserDtoRequest: " + request);
        if (request == null) {
            throw new IllegalArgumentException("UserDtoRequest cannot be null");
        }

        System.out.println("Mapping UserDtoRequest: " + request);

        if (request.getPosition() == null || request.getPosition().isBlank()) {
            throw new IllegalArgumentException("Position cannot be null or empty");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPosition(PositionEnum.valueOf(request.getPosition()));
        return user;
    }

    public UserDtoResponse mapUserToUserDtoResponse(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        UserDtoResponse dto = new UserDtoResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPosition(user.getPosition() != null ? user.getPosition().toString() : null);
        dto.setCreatedDate(user.getCreatedDate());
        dto.setUpdatedDate(user.getUpdatedDate());

        if (user.getCreatedProjects() != null) {
            dto.setCreatedProjects(
                    user.getCreatedProjects().stream()
                            .filter(Objects::nonNull)
                            .map(sharedMapper::projectToCreatedProjectsDtoResponse)
                            .collect(Collectors.toList())
            );
        }

        if (user.getUserProjects() != null) {
            dto.setUserProjects(user.getUserProjects().stream()
                    .map(userProjectMapper::mapUserProjectToUserProjectDtoResponse)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public UserSimplifiedDtoResponse toSimplifiedDto(User user) {
        return sharedMapper.toSimplifiedDto(user);
    }
}
