package org.example.server.security;

import org.example.server.exception.ProjectNotFoundException;
import org.example.server.exception.UnauthorizedProjectAccessException;
import org.example.server.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

    private final ProjectRepository projectRepository;

    public SecurityUtil(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public boolean isUserInProject(Long projectId) {
        String currentUserEmail = getCurrentUserEmail();
        log.info("Vérification des autorisations pour l'utilisateur {} sur le projet {}", currentUserEmail, projectId);

        return projectRepository.findById(projectId)
                .map(project -> {
                    boolean isCreator = project.getCreatedBy() != null
                            && project.getCreatedBy().getEmail().equals(currentUserEmail);

                    boolean isUserAssociated = project.getUserProjects() != null && project.getUserProjects().stream()
                            .anyMatch(userProject -> userProject.getUser().getEmail().equals(currentUserEmail));

                    if (isCreator) {
                        log.info("Utilisateur {} est le créateur du projet {}", currentUserEmail, projectId);
                    } else if (!isUserAssociated) {
                        log.warn("Utilisateur {} non autorisé à accéder au projet {}", currentUserEmail, projectId);
                    } else {
                        log.info("Utilisateur {} est associé au projet {}", currentUserEmail, projectId);
                    }

                    return isCreator || isUserAssociated;
                })
                .orElseThrow(() -> {
                    log.error("Projet non trouvé avec ID : {}", projectId);
                    return new ProjectNotFoundException("Projet non trouvé avec ID : " + projectId);
                });
    }

    public String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            log.error("Utilisateur non authentifié ou principal invalide : {}", principal);
            throw new UnauthorizedProjectAccessException("Utilisateur non authentifié ou principal invalide.");
        }
    }
}
