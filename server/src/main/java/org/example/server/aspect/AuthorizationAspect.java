package org.example.server.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.server.exception.ProjectNotFoundException;
import org.example.server.exception.UnauthorizedProjectAccessException;
import org.example.server.exception.UserNotFoundException;
import org.example.server.model.Project;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.UserRepository;
import org.example.server.security.SecurityUtil;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final SecurityUtil securityUtil;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Before("@annotation(CheckProjectAuthorization) && args(projectId,..)")
    public void checkAuthorizationByProjectId(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Projet non trouvé avec ID : " + projectId)
                );

        if (!securityUtil.isUserInProject(project.getId())) {
            throw new UnauthorizedProjectAccessException(
                    "Vous n'êtes pas autorisé à accéder au projet " + projectId);
        }
    }

    @Before("@annotation(CheckUserAuthorization) && args(userId,..)")
    public void checkAuthorizationByUserId(Long userId) {

        String currentUserEmail = securityUtil.getCurrentUserEmail();

        userRepository.findById(userId).ifPresentOrElse(user -> {
            if (!user.getEmail().equals(currentUserEmail)) {
                throw new UnauthorizedProjectAccessException(
                        "Vous n'êtes pas autorisé à accéder aux données de l'utilisateur avec ID : " + userId);
            }
        }, () -> {
            throw new UserNotFoundException("Utilisateur non trouvé avec ID : " + userId);
        });
    }
}