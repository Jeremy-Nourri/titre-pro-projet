package org.example.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BoardColumnDtoRequest {

    @NotBlank(message = "Le nom de la colonne ne peut pas être vide.")
    @Size(max = 50, message = "Le nom ne doit pas excédé 50 caractères")
    private String name;

    private List<TaskDtoRequest> tasks;

}