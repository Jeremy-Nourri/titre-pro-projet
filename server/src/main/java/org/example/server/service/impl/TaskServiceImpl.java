package org.example.server.service.impl;

import org.example.server.aspect.CheckProjectAuthorization;
import org.example.server.dto.request.TaskDtoRequest;
import org.example.server.exception.ProjectNotFoundException;
import org.example.server.exception.TaskNotFoundException;
import org.example.server.model.Project;
import org.example.server.model.Task;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.TaskRepository;
import org.example.server.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    @CheckProjectAuthorization
    public Task createTask(Long projectId, TaskDtoRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet non trouvé avec ID : " + projectId));

        Task task = Task.builder()
                .title(request.getTitle())
                .detail(request.getDetail())
                .priority(request.getPriority())
                .taskStatus(request.getTaskStatus())
                .dueDate(request.getDueDate())
                .project(project)
                .createdAt(LocalDate.now())
                .build();

        return taskRepository.save(task);
    }

    @Override
    @CheckProjectAuthorization
    public Task updateTask(Long taskId, TaskDtoRequest request) {

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));

        if (request.getTitle() != null) existingTask.setTitle(request.getTitle());
        if (request.getDetail() != null) existingTask.setDetail(request.getDetail());
        if (request.getPriority() != null) existingTask.setPriority(request.getPriority());
        if (request.getTaskStatus() != null) existingTask.setTaskStatus(request.getTaskStatus());
        if (request.getDueDate() != null) existingTask.setDueDate(request.getDueDate());

        existingTask.setUpdatedAt(LocalDate.now());

        return taskRepository.save(existingTask);
    }

    @Override
    @CheckProjectAuthorization
    public boolean deleteTask(Long taskId) {

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));

        taskRepository.delete(existingTask);

        Optional<Task> taskFound = taskRepository.findById(taskId);

        return taskFound.isPresent();
    }

    @Override
    public List<Task> getTasksByProject(Long projectId) {

        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet non trouvé avec ID : " + projectId));

        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));
    }
}
