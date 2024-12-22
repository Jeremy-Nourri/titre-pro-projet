package org.example.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserProjectDtoResponse {
    private Long id;
    private Long projectId;
    private String projectName;
    private String userAddedAt;
}
