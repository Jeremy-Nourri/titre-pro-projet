package org.example.server.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private String createdAt;
    private String updatedAt;

    private List<ProjectDtoResponse> createdProjects;
    private List<UserProjectDtoResponse> userProjects;

}
