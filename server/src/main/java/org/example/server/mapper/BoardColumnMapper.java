package org.example.server.mapper;

import org.example.server.dto.response.BoardColumnDtoResponse;
import org.example.server.model.BoardColumn;

public class BoardColumnMapper {

    public static BoardColumnDtoResponse toResponseDTO(BoardColumn boardColumn) {
        BoardColumnDtoResponse dto = new BoardColumnDtoResponse();
        dto.setId(boardColumn.getId());
        dto.setName(boardColumn.getName());
        dto.setProjectId(boardColumn.getProject().getId());
        dto.setTasks(TaskMapper.toTaskDTOList(boardColumn.getTasks()));
        dto.setCreatedDate(boardColumn.getCreatedDate());
        dto.setUpdatedDate(boardColumn.getUpdatedDate());
        return dto;
    }
}

