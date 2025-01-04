package org.example.server.security;

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

    public SecurityUtil(ProjectRepository projectRepository) {
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
