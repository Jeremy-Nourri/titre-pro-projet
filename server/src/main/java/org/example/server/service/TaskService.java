package org.example.server.service;

import org.example.server.dto.request.TaskDtoRequest;
import org.example.server.dto.response.TaskDtoResponse;
import org.example.server.dto.response.TaskSimplifiedDtoResponse;
import org.example.server.model.Task;

import java.util.List;

public interface TaskService {

    TaskDtoResponse createTask(Long projectId, Long boardColumnId, TaskDtoRequest request);

    TaskDtoResponse updateTask(Long projectId, Long taskId, TaskDtoRequest request);

    void deleteTask(Long projectId, Long taskId);

    List<TaskSimplifiedDtoResponse> getTasksByBoardColumnId(Long projectId, Long boardColumnId);

    TaskDtoResponse getTaskById(Long projectId, Long taskId);

    TaskDtoResponse moveTaskToColumn(Long projectId, Long taskId, Long columnId);
}
