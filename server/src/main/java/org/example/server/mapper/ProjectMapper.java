package org.example.server.mapper;

import org.example.server.dto.response.CreatedProjectsDtoResponse;
import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.model.Project;

import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static ProjectDtoResponse ProjectToProjectDtoResponse(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDtoResponse dto = new ProjectDtoResponse();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setCreatedDate(project.getCreatedDate());
        dto.setUpdatedDate(project.getUpdatedDate());

        if (project.getCreatedBy() != null) {
            dto.setCreatedBy(UserMapper.toSimplifiedDto(project.getCreatedBy()));
        }

        if (project.getUserProjects() != null) {
            dto.setUsers(project.getUserProjects().stream()
                    .map(userProject -> UserMapper.toSimplifiedDto(userProject.getUser()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }


        if (project.getColumns() != null) {
            dto.setBoardColumns(project.getColumns().stream()
                    .map(BoardColumnMapper::toResponseDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static CreatedProjectsDtoResponse ProjectToCreatedProjectsDtoResponse(Project project) {
        if (project == null) {
            return null;
        }

        CreatedProjectsDtoResponse dto = new CreatedProjectsDtoResponse();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setCreatedDate(project.getCreatedDate());
        dto.setUpdatedDate(project.getUpdatedDate());
        return dto;
    }

}
