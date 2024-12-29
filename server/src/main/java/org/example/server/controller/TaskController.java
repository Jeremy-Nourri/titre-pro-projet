package org.example.server.controller;

import org.example.server.dto.request.TaskDtoRequest;
import org.example.server.dto.response.TaskDtoResponse;
import org.example.server.dto.response.TaskSimplifiedDtoResponse;
import org.example.server.model.Task;
import org.example.server.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/columns/{boardColumnId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDtoResponse> createTask(
            @PathVariable Long projectId,
            @PathVariable Long boardColumnId,
            @Valid @RequestBody TaskDtoRequest request) {
        TaskDtoResponse createdTask = taskService.createTask(projectId, boardColumnId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDtoResponse> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskDtoRequest request) {
        TaskDtoResponse updatedTask = taskService.updateTask(projectId, taskId, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        taskService.deleteTask(projectId, taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskSimplifiedDtoResponse>> getTasksByBoardColum(
            @PathVariable Long projectId,
            @PathVariable Long boardColumnId) {
        List<TaskSimplifiedDtoResponse> tasks = taskService.getTasksByBoardColumnId(projectId, boardColumnId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDtoResponse> getTaskById(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        TaskDtoResponse task = taskService.getTaskById(projectId, taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}/move")
    public ResponseEntity<TaskDtoResponse> moveTaskToColumn(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long boardColumnId
    ) {
        TaskDtoResponse response = taskService.moveTaskToColumn(projectId, taskId, boardColumnId);
        return ResponseEntity.ok(response);
    }
}
