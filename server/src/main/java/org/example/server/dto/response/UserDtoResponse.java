package org.example.server.dto.response;

import lombok.Data;
import org.example.server.model.PositionEnum;

@Data
public class UserDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private PositionEnum position;
    private String createdAt;
    private String updatedAt;

}
