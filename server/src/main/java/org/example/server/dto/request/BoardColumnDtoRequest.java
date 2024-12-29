package org.example.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BoardColumnDtoRequest {

    @NotBlank(message = "Le nom de la colonne ne peut pas être vide.")
    private String name;

    private List<TaskDtoRequest> tasks;

}