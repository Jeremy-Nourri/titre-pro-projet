package org.example.server.dto.response;

import lombok.Data;

@Data
public class TaskSimplifiedDtoResponse {
    private Long id;
    private String title;
    private String priority;
}
