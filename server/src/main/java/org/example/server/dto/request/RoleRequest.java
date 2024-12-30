package org.example.server.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.server.model.RoleEnum;

@Data
public class RoleRequest {

    @NotNull(message = "Le rôle est requis")
    private RoleEnum role;

    @NotNull(message = "L'email de l'utilisateur est requis")
    private String userEmail;
}
