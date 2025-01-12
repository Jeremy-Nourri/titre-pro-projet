package org.example.server.mapper;

import org.example.server.dto.response.BoardColumnDtoResponse;
import org.example.server.model.BoardColumn;
import org.springframework.stereotype.Component;

@Component
public class BoardColumnMapper {

    private final TaskMapper taskMapper;

    public BoardColumnMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public BoardColumnDtoResponse toResponseDTO(BoardColumn boardColumn) {
        if (boardColumn == null) {
            return null;
        }

        BoardColumnDtoResponse dto = new BoardColumnDtoResponse();
        dto.setId(boardColumn.getId());
        dto.setName(boardColumn.getName());
        dto.setProjectId(boardColumn.getProject().getId());
        dto.setTasks(taskMapper.toTaskDTOList(boardColumn.getTasks()));
        dto.setCreatedDate(boardColumn.getCreatedDate());
        dto.setUpdatedDate(boardColumn.getUpdatedDate());
        return dto;
    }
}


