package org.example.server.mapper;

import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.exception.UnauthorizedProjectAccessException;
import org.example.server.model.Project;
import org.example.server.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

public class ProjectMapper {

    public static ProjectDtoResponse ProjectToProjectDtoResponse(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDtoResponse dto = new ProjectDtoResponse();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate().toString());
        dto.setEndDate(project.getEndDate().toString());
        dto.setCreatedDate(project.getCreatedDate().toString());
        dto.setUpdatedDate(project.getUpdatedDate() != null ? project.getUpdatedDate().toString() : null);

        if (project.getCreatedBy() != null) {
            dto.setCreatedBy(UserMapper.toSimplifiedDto(project.getCreatedBy()));
        }
        if (project.getUserProjects() != null) {
            dto.setUsers(project.getUserProjects().stream()
                    .map(userProject -> UserMapper.toSimplifiedDto(userProject.getUser()))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static Project PorjectDtoRequestToProject(ProjectDtoRequest dto, User creator) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setCreatedBy(creator);

        return project;
    }

    private void ensureUserInProject(Project project, UserDetails currentUser) {
        boolean isUserInProject = project.getUserProjects().stream()
                .anyMatch(userProject -> userProject.getUser().getEmail().equals(currentUser.getUsername()));

        if (!isUserInProject) {
            throw new UnauthorizedProjectAccessException("Vous n'êtes pas autorisé à accéder à ce projet.");
        }
    }
}
