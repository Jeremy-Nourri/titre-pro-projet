package org.example.server.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDtoRequest {

    @NotBlank(message = "Le prénom est requis")
    @Size(max = 100, message = "Le prénom ne doit pas excédé 100 caractères")
    private String firstName;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne doit pas excédé 100 caractères")
    private String lastName;

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    @Size(max = 150, message = "L'email ne doit pas excédé 150 caractères")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Size(max = 255, message = "Le mot de passe ne doit pas excédé 255 caractères")
    private String password;

    @NotNull(message = "Le poste est requis")
    private String position;

}
