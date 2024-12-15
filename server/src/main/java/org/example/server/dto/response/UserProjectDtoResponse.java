package org.example.server.dto.response;

import lombok.Data;

@Data
public class UserProjectDtoResponse {
    private Long id;
    private Long projectId;
    private String projectName;
    private String userAddedAt;
}
