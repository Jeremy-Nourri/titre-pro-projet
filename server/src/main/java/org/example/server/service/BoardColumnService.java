package org.example.server.service;

import org.example.server.dto.request.BoardColumnDtoRequest;
import org.example.server.dto.response.BoardColumnDtoResponse;

public interface BoardColumnService {

    BoardColumnDtoResponse createBoardColumn(Long projectId, BoardColumnDtoRequest boardColumnDtoRequest);
    BoardColumnDtoResponse getBoardColumn(Long columnId);
    BoardColumnDtoResponse updateBoardColumn(Long columnId, Long projectId, BoardColumnDtoRequest boardColumnDtoRequest);
    void deleteBoardColumn(Long columnId);
}
