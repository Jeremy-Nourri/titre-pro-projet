package org.example.server.service;

import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.model.Project;
import org.springframework.security.core.userdetails.UserDetails;

public interface ProjectService {
    Project createProject(ProjectDtoRequest request, UserDetails currentUser);
    Project updateProject(Long projectId, ProjectDtoRequest request);
    void deleteProject(Long projectId);
    void addUserToProject(Long projectId, Long userId);

}

