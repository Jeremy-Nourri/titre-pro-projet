package org.example.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate updatedDate;

    private List<CreatedProjectsDtoResponse> createdProjects;
    private List<UserProjectDtoResponse> userProjects;

}
