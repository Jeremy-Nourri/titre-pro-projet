package org.example.server.service;

import org.example.server.dto.request.BoardColumnDtoRequest;
import org.example.server.dto.response.BoardColumnDtoResponse;

public interface BoardColumnService {

    BoardColumnDtoResponse createBoardColumn(Long projectId, BoardColumnDtoRequest boardColumnDtoRequest);
    BoardColumnDtoResponse getBoardColumn(Long projectId, Long columnId);
    BoardColumnDtoResponse updateBoardColumn(Long projectId, Long columnId, BoardColumnDtoRequest boardColumnDtoRequest);
    void deleteBoardColumn(Long projectId, Long columnId);
}
