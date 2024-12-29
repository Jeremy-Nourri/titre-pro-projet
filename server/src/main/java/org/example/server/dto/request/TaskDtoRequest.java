package org.example.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.server.model.PriorityEnum;
import org.example.server.model.TaskStatusEnum;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoRequest {

    private Long id;

    @NotBlank
    private String title;

    private String detail;

    @NotNull
    private PriorityEnum priority;

    @NotNull
    private TaskStatusEnum taskStatus;

    @NotNull
    private Long boardColumnId;

    @NotNull
    private LocalDate dueDate;

    private List<TagDtoRequest> tags;

}
