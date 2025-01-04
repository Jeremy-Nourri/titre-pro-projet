package org.example.server.mapper;

import org.example.server.dto.response.UserProjectDtoResponse;
import org.example.server.model.UserProject;

public class UserProjectMapper {
    public static UserProjectDtoResponse mapUserProjectToUserProjectDtoResponse(UserProject userProject) {
        if (userProject == null) {
            return null;
        }

        UserProjectDtoResponse dto = new UserProjectDtoResponse();
        dto.setId(userProject.getId());
        dto.setProjectId(userProject.getProject().getId());
        dto.setProjectName(userProject.getProject().getName());
        dto.setUserAddedAt(userProject.getUserAddAt());
        dto.setCreatedDate(userProject.getCreatedDate());
        dto.setUpdatedDate(userProject.getUpdatedDate());
        return dto;
    }
}
