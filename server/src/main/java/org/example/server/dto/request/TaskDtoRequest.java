package org.example.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.server.model.PriorityEnum;
import org.example.server.model.TaskStatusEnum;

import java.time.LocalDate;

@Builder
@Data
public class TaskDtoRequest {

    @NotBlank
    private String title;

    private String detail;

    @NotNull
    private PriorityEnum priority;

    @NotNull
    private TaskStatusEnum taskStatus;

    @NotNull
    private LocalDate dueDate;

}
