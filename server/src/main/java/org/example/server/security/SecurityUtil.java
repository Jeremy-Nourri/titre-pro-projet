package org.example.server.security;

import org.example.server.exception.UnauthorizedProjectAccessException;
import org.example.server.repository.ProjectRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private final ProjectRepository projectRepository;

    public SecurityUtil(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public boolean isUserInProject(Long projectId) {
        String currentUserEmail = getCurrentUserEmail();
        return projectRepository.findById(projectId)
                .map(project -> project.getUserProjects().stream()
                        .anyMatch(userProject -> userProject.getUser().getEmail().equals(currentUserEmail)))
                .orElseThrow(() -> new UnauthorizedProjectAccessException("Projet non trouvé avec ID : " + projectId));
    }

    private String getCurrentUserEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
