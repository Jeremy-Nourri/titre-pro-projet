package org.example.server.mapper;

import org.example.server.dto.response.CreatedProjectsDtoResponse;
import org.example.server.dto.response.UserSimplifiedDtoResponse;
import org.example.server.model.Project;
import org.example.server.model.User;
import org.springframework.stereotype.Component;

@Component
public class SharedMapper {

    public UserSimplifiedDtoResponse toSimplifiedDto(User user) {
        if (user == null) {
            return null;
        }

        UserSimplifiedDtoResponse dto = new UserSimplifiedDtoResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPosition(user.getPosition() != null ? user.getPosition().toString() : null);
        return dto;
    }

    public CreatedProjectsDtoResponse projectToCreatedProjectsDtoResponse(Project project) {
        if (project == null) {
            return null;
        }

        CreatedProjectsDtoResponse dto = new CreatedProjectsDtoResponse();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setCreatedDate(project.getCreatedDate());
        dto.setUpdatedDate(project.getUpdatedDate());
        return dto;
    }
}
