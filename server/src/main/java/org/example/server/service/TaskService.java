package org.example.server.service;

import org.example.server.dto.request.TaskDtoRequest;
import org.example.server.model.Task;

import java.util.List;

public interface TaskService {

    Task createTask(Long projectId, TaskDtoRequest request);

    Task updateTask(Long taskId, TaskDtoRequest request);

    void deleteTask(Long taskId);

    List<Task> getTasksByProject(Long projectId);

    Task getTaskById(Long taskId);
}
