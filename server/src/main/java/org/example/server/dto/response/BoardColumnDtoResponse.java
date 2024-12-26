package org.example.server.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BoardColumnDtoResponse {

    private Long id;
    private String name;
    private Long projectId;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private List<TaskSimplifiedDtoResponse> tasks;
}