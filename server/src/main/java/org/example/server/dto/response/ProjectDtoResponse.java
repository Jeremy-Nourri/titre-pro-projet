package org.example.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProjectDtoResponse {
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String createdDate;
    private String updatedDate;
    private UserSimplifiedDtoResponse createdBy;
    private List<UserSimplifiedDtoResponse> users;
}
