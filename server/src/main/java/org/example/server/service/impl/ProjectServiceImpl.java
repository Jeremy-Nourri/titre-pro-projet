package org.example.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.server.aspect.CheckProjectAuthorization;
import org.example.server.aspect.CheckUserAuthorization;
import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.exception.InvalidProjectDateException;
import org.example.server.exception.ProjectNotFoundException;
import org.example.server.exception.UserAlreadyAssignedException;
import org.example.server.exception.UserNotFoundException;
import org.example.server.mapper.ProjectMapper;
import org.example.server.model.Project;
import org.example.server.model.User;
import org.example.server.model.UserProject;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.ProjectService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectDtoResponse createProject(ProjectDtoRequest request, UserDetails currentUser) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new InvalidProjectDateException("La date de fin ne peut pas être antérieure à la date de début.");
        }

        User creator = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdBy(creator)
                .createdDate(LocalDate.now())
                .build();

        Project savedProject = projectRepository.save(project);
        return ProjectMapper.ProjectToProjectDtoResponse(savedProject);
    }

    @Override
    @CheckUserAuthorization
    @Transactional(readOnly = true)
    public List<ProjectDtoResponse> getProjectsByUserId(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec ID " + userId + " non trouvé"));

        List<Project> projects = projectRepository.findAllByUserId(userId);

        return projects.stream().map(ProjectMapper::ProjectToProjectDtoResponse).toList();
    }

    @Override
    @CheckProjectAuthorization
    @Transactional
    public ProjectDtoResponse updateProject(Long projectId, ProjectDtoRequest request) {

        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet avec ID " + projectId + " non trouvé"));

        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new InvalidProjectDateException("La date de fin ne peut pas être antérieure à la date de début.");
            }
        }

        if (request.getName() != null) {
            existingProject.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingProject.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
            existingProject.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            existingProject.setEndDate(request.getEndDate());
        }

        Project updatedProject = projectRepository.save(existingProject);

        return ProjectMapper.ProjectToProjectDtoResponse(updatedProject);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional
    public void deleteProject(Long projectId) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet avec ID " + projectId + " non trouvé"));

        projectRepository.delete(existingProject);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional
    public void addUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet avec ID " + projectId + " non trouvé"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec ID " + userId + " non trouvé"));

        boolean userAlreadyAssigned = project.getUserProjects().stream()
                .anyMatch(userProject -> userProject.getUser().getId().equals(userId));

        if (userAlreadyAssigned) {
            throw new UserAlreadyAssignedException("L'utilisateur est déjà assigné à ce projet.");
        }

        UserProject userProject = UserProject.builder()
                .user(user)
                .project(project)
                .userAddAt(LocalDate.now())
                .build();

        project.getUserProjects().add(userProject);
        projectRepository.save(project);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional(readOnly = true)
    public ProjectDtoResponse getProjectById(Long projectId) {
        Project projectFound =  projectRepository.findByIdWithColumns(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet avec ID " + projectId + " non trouvé"));
        return ProjectMapper.ProjectToProjectDtoResponse(projectFound);
    }
}
