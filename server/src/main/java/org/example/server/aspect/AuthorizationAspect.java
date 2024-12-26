package org.example.server.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.server.exception.BoardColumnNotFoundException;
import org.example.server.exception.ProjectNotFoundException;
import org.example.server.exception.TaskNotFoundException;
import org.example.server.exception.UnauthorizedProjectAccessException;
import org.example.server.model.BoardColumn;
import org.example.server.model.Project;
import org.example.server.model.Task;
import org.example.server.repository.BoardColumnRepository;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.TaskRepository;
import org.example.server.security.SecurityUtil;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final SecurityUtil securityUtil;
    private final ProjectRepository projectRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final TaskRepository taskRepository;

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


    @Before("@annotation(CheckProjectAuthorization) && args(boardColumnId,..)")
    public void checkAuthorizationByBoardColumnId(Long boardColumnId) {

        BoardColumn column = boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new BoardColumnNotFoundException(
                        "Colonne non trouvée avec ID : " + boardColumnId)
                );
        Long projectId = column.getProject().getId();

        if (!securityUtil.isUserInProject(projectId)) {
            throw new UnauthorizedProjectAccessException(
                    "Vous n'êtes pas autorisé à accéder à la colonne " + boardColumnId
                            + " (projet " + projectId + ")");
        }
    }

    @Before("@annotation(CheckProjectAuthorization) && args(taskId,..)")
    public void checkAuthorizationByTaskId(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Tâche non trouvée avec ID : " + taskId)
                );

        BoardColumn column = task.getBoardColumn();
        if (column == null) {
            throw new BoardColumnNotFoundException("La tâche " + taskId
                    + " n'est pas rattachée à une colonne valide !");
        }
        Long projectId = column.getProject().getId();

        if (!securityUtil.isUserInProject(projectId)) {
            throw new UnauthorizedProjectAccessException(
                    "Vous n'êtes pas autorisé à accéder à la tâche " + taskId
                            + " (projet " + projectId + ")");
        }
    }
}

