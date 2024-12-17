package org.example.server.dto.response;

import lombok.Data;

@Data
public class TagDtoResponse {
    private Long id;
    private String designation;
    private Long taskId;
}
