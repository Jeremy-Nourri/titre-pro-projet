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
    public ResponseEntity<TaskDtoResponse> createTask(@PathVariable Long projectId, @Valid @RequestBody TaskDtoRequest request) {
        TaskDtoResponse createdTask = taskService.createTask(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDtoResponse> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskDtoRequest request) {
        TaskDtoResponse updatedTask = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskSimplifiedDtoResponse>> getTasksByBoardColum(@PathVariable Long boardColumnId) {
        List<TaskSimplifiedDtoResponse> tasks = taskService.getTasksByBoardColumnId(boardColumnId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDtoResponse> getTaskById(@PathVariable Long taskId) {
        TaskDtoResponse task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }
}
