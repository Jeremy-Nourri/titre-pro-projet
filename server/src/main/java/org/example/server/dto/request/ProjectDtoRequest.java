package org.example.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data

public class ProjectDtoRequest {

    @NotBlank(message = "Le nom est requis")
    @Size(max = 50, message = "Le nom ne doit pas excédé 50 caractères")
    private String name;

    @Size(max = 100, message = "La description ne doit pas excédée 100 caractères")
    private String description;

    @NotNull(message = "La date de début est requise")
    @PastOrPresent(message = "La date de début doit être dans le passé ou aujourd'hui")
    private LocalDate startDate;

    @NotNull(message = "La date de fin est requise")
    @Future(message = "La date de fin doit être dans le futur")
    private LocalDate endDate;

    @NotNull(message = "L'id de l'utilisateur est requis")
    private Long createdBy;

}
