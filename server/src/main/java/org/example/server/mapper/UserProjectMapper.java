package org.example.server.mapper;

import org.example.server.dto.response.UserProjectDtoResponse;
import org.example.server.model.UserProject;

public class UserProjectMapper {
    public static UserProjectDtoResponse mapUserProjectToUserProjectDtoResponse(UserProject userProject) {
        if (userProject == null) {
            return null;
        }

        UserProjectDtoResponse response = new UserProjectDtoResponse();
        response.setId(userProject.getId());
        response.setProjectId(userProject.getProject().getId());
        response.setProjectName(userProject.getProject().getName());
        response.setUserAddedAt(userProject.getUserAddAt() != null ? userProject.getUserAddAt().toString() : null);
        return response;
    }
}
