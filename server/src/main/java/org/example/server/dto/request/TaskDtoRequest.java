package org.example.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.server.model.PriorityEnum;
import org.example.server.model.TaskStatusEnum;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoRequest {

    private Long id;

    @NotBlank
    @Size(max = 50, message = "Le titre ne doit pas excédé 50 caractères")
    private String title;

    @Size(max = 300, message = "Le détail ne doit pas excédé 300 caractères")
    private String detail;

    @NotNull
    private PriorityEnum priority;

    @NotNull
    private TaskStatusEnum taskStatus;

    @NotNull
    private Long boardColumnId;

    @NotNull
    private LocalDate dueDate;

    @Size(max = 20, message = "Le nom du tag ne doit pas excédé 20 caractères")
    private String tag;

    @Size(max = 7, message = "La couleur doit être une valeur hexadécimale de 7 caractères")
    private String tagColor;

}
