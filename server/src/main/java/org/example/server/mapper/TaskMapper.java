package org.example.server.mapper;

import org.example.server.dto.response.TaskDtoResponse;
import org.example.server.dto.response.TaskSimplifiedDtoResponse;
import org.example.server.model.Task;

import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskDtoResponse TaskToTaskDtoResponse(Task task) {
        if (task == null) {
            return null;
        }

        TaskDtoResponse taskDto = new TaskDtoResponse();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDetail(task.getDetail());
        taskDto.setPriority(task.getPriority().name());
        taskDto.setTaskStatus(task.getTaskStatus().name());
        taskDto.setDueDate(task.getDueDate());

        return taskDto;
    }

    public static TaskSimplifiedDtoResponse toTaskSummaryDTO(Task task) {
        if (task == null) {
            return null;
        }
        TaskSimplifiedDtoResponse dto = new TaskSimplifiedDtoResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setPriority(task.getPriority().name());
        return dto;
    }

    public static List<TaskSimplifiedDtoResponse> toTaskSummaryDTOList(List<Task> tasks) {
        return tasks.stream()
                .map(TaskMapper::toTaskSummaryDTO)
                .collect(Collectors.toList());
    }

}
