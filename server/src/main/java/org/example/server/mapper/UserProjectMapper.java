package org.example.server.mapper;

import org.example.server.dto.response.UserProjectDtoResponse;
import org.example.server.model.UserProject;
import org.springframework.stereotype.Component;

@Component
public class UserProjectMapper {

    public UserProjectDtoResponse mapUserProjectToUserProjectDtoResponse(UserProject userProject) {
        if (userProject == null) {
            return null;
        }

        UserProjectDtoResponse dto = new UserProjectDtoResponse();
        dto.setId(userProject.getId());
        dto.setProjectId(userProject.getProject().getId());
        dto.setProjectName(userProject.getProject().getName());
        dto.setDescription(userProject.getProject().getDescription());
        dto.setEndDate(userProject.getProject().getEndDate());
        dto.setUserAddedAt(userProject.getUserAddAt());
        dto.setRoleEnum(userProject.getRole());
        dto.setCreatedDate(userProject.getCreatedDate());
        dto.setUpdatedDate(userProject.getUpdatedDate());

        return dto;
    }
}
