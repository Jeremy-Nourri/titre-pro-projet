package org.example.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagDtoRequest {
    @NotBlank(message = "Designation is required")
    private String designation;

    private String color;
}
