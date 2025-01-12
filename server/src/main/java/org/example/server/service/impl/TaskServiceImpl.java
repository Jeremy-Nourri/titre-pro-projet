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
import org.example.server.model.RoleEnum;
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
    private final TaskMapper taskMapper;


    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public TaskDtoResponse createTask(Long projectId, Long boardColumnId, TaskDtoRequest request) {

        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId)
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
                .tag(request.getTag())
                .tagColor(request.getTagColor())
                .build();

        Task taskSaved = taskRepository.save(task);

        return taskMapper.taskToTaskDtoResponse(taskSaved);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public TaskDtoResponse updateTask(Long projectId, Long taskId, TaskDtoRequest request) {

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));

        BoardColumn boardColumn = boardColumnRepository.findById(request.getBoardColumnId())
                .orElseThrow(() -> new BoardColumnNotFoundException(
                        "Colonne non trouvée avec ID : " + request.getBoardColumnId())
                );

        if (request.getTitle() != null) existingTask.setTitle(request.getTitle());
        if (request.getDetail() != null) existingTask.setDetail(request.getDetail());
        if (request.getPriority() != null) existingTask.setPriority(request.getPriority());
        if (request.getTaskStatus() != null) existingTask.setTaskStatus(request.getTaskStatus());
        if (request.getDueDate() != null) existingTask.setDueDate(request.getDueDate());
        if (request.getBoardColumnId() != null) existingTask.setBoardColumn(boardColumn);
        if (request.getTag() != null) existingTask.setTag(request.getTag());
        if (request.getTagColor() != null) existingTask.setTagColor(request.getTagColor());

        return taskMapper.taskToTaskDtoResponse(existingTask);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public void deleteTask(Long projectId, Long taskId) {

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));

        taskRepository.delete(existingTask);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN, RoleEnum.MEMBER}, isNeedWriteAccess = true)
    @Transactional(readOnly = true)
    public List<TaskSimplifiedDtoResponse> getTasksByBoardColumnId(Long projectId, Long boardColumnId) {

        boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new BoardColumnNotFoundException(
                        "Colonne non trouvée avec ID : " + boardColumnId)
                );

        List<Task> tasks = taskRepository.findTasksByBoardColumnId(boardColumnId);

        return taskMapper.toTaskSummaryDTOList(tasks);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN, RoleEnum.MEMBER}, isNeedWriteAccess = true)
    @Transactional(readOnly = true)
    public TaskDtoResponse getTaskById(Long projectId, Long taskId) {
        Task taskFound = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec ID : " + taskId));

        return taskMapper.taskToTaskDtoResponse(taskFound);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public TaskDtoResponse moveTaskToColumn(Long projectId, Long taskId, Long targetColumnId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée avec l'id : " + taskId));

        BoardColumn targetColumn = boardColumnRepository.findById(targetColumnId)
                .orElseThrow(() -> new BoardColumnNotFoundException("Colonne non trouvée avec l'id : " + targetColumnId));

        task.setBoardColumn(targetColumn);

        Task updatedTask = taskRepository.save(task);

        return taskMapper.taskToTaskDtoResponse(updatedTask);
    }

}
