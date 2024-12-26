package org.example.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BoardColumnDtoRequest {

    @NotBlank(message = "Le nom de la colonne ne peut pas être vide.")
    private String name;

}