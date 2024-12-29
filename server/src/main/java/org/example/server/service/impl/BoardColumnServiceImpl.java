package org.example.server.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.server.aspect.CheckProjectAuthorization;
import org.example.server.dto.request.BoardColumnDtoRequest;
import org.example.server.dto.response.BoardColumnDtoResponse;
import org.example.server.exception.BoardColumnNotFoundException;
import org.example.server.exception.ProjectNotFoundException;
import org.example.server.mapper.BoardColumnMapper;
import org.example.server.mapper.TaskMapper;
import org.example.server.model.BoardColumn;
import org.example.server.model.Project;
import org.example.server.model.Task;
import org.example.server.repository.BoardColumnRepository;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.TaskRepository;
import org.example.server.service.BoardColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardColumnServiceImpl implements BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    @CheckProjectAuthorization
    @Transactional
    public BoardColumnDtoResponse createBoardColumn(Long projectId, BoardColumnDtoRequest requestDTO) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet non trouvé avec l'id : " + projectId));

        BoardColumn boardColumn = BoardColumn.builder()
                .name(requestDTO.getName())
                .project(project)
                .build();

        BoardColumn savedColumn = boardColumnRepository.save(boardColumn);

        return BoardColumnMapper.toResponseDTO(savedColumn);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional(readOnly = true)
    public BoardColumnDtoResponse getBoardColumn(Long projectId, Long columnId) {
        BoardColumn column = boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new BoardColumnNotFoundException("Column board non trouvée avec l'id : " + columnId));

        return BoardColumnMapper.toResponseDTO(column);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional
    public BoardColumnDtoResponse updateBoardColumn(Long projectId, Long columnId, BoardColumnDtoRequest requestDTO) {

        BoardColumn column = boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new BoardColumnNotFoundException("Column board non trouvée avec l'id : " + columnId));

        if (!Objects.equals(column.getProject().getId(), projectId)) {
            Project newProject = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ProjectNotFoundException("Projet non trouvé avec l'id : " + projectId));
            column.setProject(newProject);
        }

        if (requestDTO.getName() != null) {
            column.setName(requestDTO.getName());
        }

        BoardColumn updatedColumn = boardColumnRepository.save(column);

        return BoardColumnMapper.toResponseDTO(updatedColumn);
    }



    @Override
    @CheckProjectAuthorization
    @Transactional(readOnly = true)
    public void deleteBoardColumn(Long projectId, Long columnId) {

        Optional<BoardColumn> optionalColumn = boardColumnRepository.findById(columnId);
        if (optionalColumn.isEmpty()) {
            throw new BoardColumnNotFoundException("Column board non trouvée avec l'id : " + columnId);
        }

        boardColumnRepository.deleteById(columnId);
    }
}
