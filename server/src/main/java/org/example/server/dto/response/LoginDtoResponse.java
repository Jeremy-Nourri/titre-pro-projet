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
public class LoginDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private String createdAt;
    private String updatedAt;
    private String token;

    private List<ProjectDtoResponse> createdProjects;
    private List<UserProjectDtoResponse> userProjects;


}
