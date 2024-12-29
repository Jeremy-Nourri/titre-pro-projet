package org.example.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.server.dto.request.BoardColumnDtoRequest;
import org.example.server.dto.response.BoardColumnDtoResponse;
import org.example.server.service.BoardColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/columns")
@RequiredArgsConstructor
public class BoardColumnController {

    private final BoardColumnService boardColumnService;

    @PostMapping
    public ResponseEntity<BoardColumnDtoResponse> createBoardColumn(
            @PathVariable Long projectId,
            @Valid @RequestBody BoardColumnDtoRequest requestDTO
    ) {
        BoardColumnDtoResponse response = boardColumnService.createBoardColumn(projectId, requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{columnId}")
    public ResponseEntity<BoardColumnDtoResponse> getBoardColumn(
            @PathVariable Long projectId,
            @PathVariable Long columnId
    ) {
        BoardColumnDtoResponse response = boardColumnService.getBoardColumn(projectId, columnId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{columnId}")
    public ResponseEntity<BoardColumnDtoResponse> updateBoardColumn(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @Valid @RequestBody BoardColumnDtoRequest requestDTO
    ) {
        BoardColumnDtoResponse response = boardColumnService.updateBoardColumn(projectId, columnId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<Void> deleteBoardColumn(
            @PathVariable Long projectId,
            @PathVariable Long columnId
    ) {
        boardColumnService.deleteBoardColumn(projectId, columnId);
        return ResponseEntity.noContent().build();
    }
}
