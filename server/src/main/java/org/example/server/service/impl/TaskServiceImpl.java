package org.example.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.server.aspect.CheckProjectAuthorization;
import org.example.server.dto.request.TaskDtoRequest;
import org.example.server.dto.response.TaskDtoResponse;
import org.example.server.dto.response.TaskSimplifiedDtoResponse;
import org.example.server.exception.BoardColumnNotFoundException;
import org.example.server.exception.TaskNotFoundException;
import org.example.server.mapper.TaskMapper;
import org.example.server.model.BoardColumn;
import org.example.server.model.Task;
import org.example.server.repository.BoardColumnRepository;
import org.example.server.repository.TaskRepository;
import org.example.server.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final BoardColumnRepository boardColumnRepository;
    private final TaskRepository taskRepository;

    @Override
    @CheckProjectAuthorization
    @Transactional
    public TaskDtoResponse createTask(Long taskId, TaskDtoRequest request) {

        BoardColumn boardColumn = boardColumnRepository.findById(request.getBoardColumnId())
                .orElseThrow(() -> new BoardColumnNotFoundException(
                        "Colonne non trouvée avec ID : " + request.getBoardColumnId())
                );

        Task task = Task.builder()
                .title(request.getTitle())
                .detail(request.getDetail())
                .priority(request.getPriority())
                .taskStatus(request.getTaskStatus())
                .dueDate(request.getDueDate())
                .boardColumn(boardColumn)
                .build();

        Task taskSaved = taskRepository.save(task);

        return TaskMapper.TaskToTaskDtoResponse(taskSaved);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional
    public TaskDtoResponse updateTask(Long taskId, TaskDtoRequest request) {

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));

        if (request.getTitle() != null) existingTask.setTitle(request.getTitle());
        if (request.getDetail() != null) existingTask.setDetail(request.getDetail());
        if (request.getPriority() != null) existingTask.setPriority(request.getPriority());
        if (request.getTaskStatus() != null) existingTask.setTaskStatus(request.getTaskStatus());
        if (request.getDueDate() != null) existingTask.setDueDate(request.getDueDate());

        Task savedTask = taskRepository.save(existingTask);

        return TaskMapper.TaskToTaskDtoResponse(savedTask);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional
    public void deleteTask(Long taskId) {

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));

        taskRepository.delete(existingTask);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional(readOnly = true)
    public List<TaskSimplifiedDtoResponse> getTasksByBoardColumnId(Long boardColumnId) {

        boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new BoardColumnNotFoundException(
                        "Colonne non trouvée avec ID : " + boardColumnId)
                );

        List<Task> tasks = taskRepository.findTasksByBoardColumnId(boardColumnId);

        return TaskMapper.toTaskSummaryDTOList(tasks);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional(readOnly = true)
    public TaskDtoResponse getTaskById(Long taskId) {
        Task taskFound = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));

        return TaskMapper.TaskToTaskDtoResponse(taskFound);
    }
}
