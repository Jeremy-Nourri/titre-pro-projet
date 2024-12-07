package org.example.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
