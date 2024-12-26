package org.example.server.service;

import org.example.server.dto.request.TaskDtoRequest;
import org.example.server.dto.response.TaskDtoResponse;
import org.example.server.dto.response.TaskSimplifiedDtoResponse;
import org.example.server.model.Task;

import java.util.List;

public interface TaskService {

    TaskDtoResponse createTask(Long projectId, TaskDtoRequest request);

    TaskDtoResponse updateTask(Long taskId, TaskDtoRequest request);

    void deleteTask(Long taskId);

    List<TaskSimplifiedDtoResponse> getTasksByBoardColumnId(Long boardColumnId);

    TaskDtoResponse getTaskById(Long taskId);
}
