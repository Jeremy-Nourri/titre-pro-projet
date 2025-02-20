package org.example.server.service;

import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.model.RoleEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ProjectService {
    ProjectDtoResponse createProject(ProjectDtoRequest request, UserDetails currentUser);
    ProjectDtoResponse updateProject(Long projectId, ProjectDtoRequest request);
    void deleteProject(Long projectId);
    void addUserToProject(Long projectId, String userEmail, RoleEnum role);
    void assignRoleToUserInProject(Long projectId, String userEmail, RoleEnum role);
    ProjectDtoResponse getProjectById(Long projectId);
    List<ProjectDtoResponse> getProjectsByUserId(Long userId);
}

