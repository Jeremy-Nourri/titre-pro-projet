package org.example.server.mapper;

import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.model.Project;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    private final SharedMapper sharedMapper;
    private final BoardColumnMapper boardColumnMapper;

    public ProjectMapper(SharedMapper sharedMapper, BoardColumnMapper boardColumnMapper) {
        this.sharedMapper = sharedMapper;
        this.boardColumnMapper = boardColumnMapper;
    }

    public ProjectDtoResponse projectToProjectDtoResponse(Project project) {
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
            dto.setCreatedBy(sharedMapper.toSimplifiedDto(project.getCreatedBy()));
        }

        if (project.getUserProjects() != null) {
            dto.setUsers(project.getUserProjects().stream()
                    .map(userProject -> sharedMapper.toSimplifiedDto(userProject.getUser()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }

        if (project.getColumns() != null) {
            dto.setBoardColumns(project.getColumns().stream()
                    .map(boardColumnMapper::toResponseDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
