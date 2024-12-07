package org.example.server.dto.response;

import lombok.Data;

@Data
public class UserDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private String createdAt;
    private String updatedAt;

}
