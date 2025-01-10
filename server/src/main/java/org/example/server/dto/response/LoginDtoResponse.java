package org.example.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate updatedDate;
    private List<CreatedProjectsDtoResponse> createdProjects;
    private List<UserProjectDtoResponse> userProjects;
    private String token;

}
