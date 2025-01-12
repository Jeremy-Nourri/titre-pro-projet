package org.example.server.mapper;

import org.example.server.dto.response.TaskDtoResponse;
import org.example.server.dto.response.TaskSimplifiedDtoResponse;
import org.example.server.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskDtoResponse taskToTaskDtoResponse(Task task) {
        if (task == null) {
            return null;
        }

        TaskDtoResponse dto = new TaskDtoResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDetail(task.getDetail());
        dto.setPriority(task.getPriority().name());
        dto.setTaskStatus(task.getTaskStatus().name());
        dto.setDueDate(task.getDueDate());
        dto.setBoardColumnId(task.getBoardColumn().getId());
        dto.setTag(task.getTag());
        dto.setTagColor(task.getTagColor());
        dto.setCreatedDate(task.getCreatedDate());
        dto.setUpdatedDate(task.getUpdatedDate());

        return dto;
    }

    public List<TaskDtoResponse> toTaskDTOList(List<Task> tasks) {
        if (tasks == null) {
            return List.of();
        }
        return tasks.stream()
                .map(this::taskToTaskDtoResponse)
                .collect(Collectors.toList());
    }

    public TaskSimplifiedDtoResponse toTaskSummaryDTO(Task task) {
        if (task == null) {
            return null;
        }

        TaskSimplifiedDtoResponse dto = new TaskSimplifiedDtoResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setPriority(task.getPriority().name());

        return dto;
    }

    public List<TaskSimplifiedDtoResponse> toTaskSummaryDTOList(List<Task> tasks) {
        if (tasks == null) {
            return List.of();
        }
        return tasks.stream()
                .map(this::toTaskSummaryDTO)
                .collect(Collectors.toList());
    }
}
