package org.example.server.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.server.exception.UnauthorizedProjectAccessException;
import org.example.server.security.SecurityUtil;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

    private final SecurityUtil securityUtil;

    public AuthorizationAspect(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @Before("@annotation(CheckProjectAuthorization) && args(projectId,..)")
    public void checkAuthorization(Long projectId) {
        if (!securityUtil.isUserInProject(projectId)) {
            throw new UnauthorizedProjectAccessException("Vous n'êtes pas autorisé à accéder à ce projet.");
        }
    }
}
