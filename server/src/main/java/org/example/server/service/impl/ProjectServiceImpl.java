package org.example.server.service.impl;

import org.example.server.aspect.CheckProjectAuthorization;
import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.exception.InvalidProjectDateException;
import org.example.server.exception.ProjectNotFoundException;
import org.example.server.model.Project;
import org.example.server.model.User;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.ProjectService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Project createProject(ProjectDtoRequest request, UserDetails currentUser) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new InvalidProjectDateException("La date de fin ne peut pas être antérieure à la date de début.");
        }

        User creator = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdBy(creator)
                .createdDate(LocalDate.now())
                .build();

        return projectRepository.save(project);
    }


    @Override
    @CheckProjectAuthorization
    public Project updateProject(Long projectId, ProjectDtoRequest request) {

        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet avec ID " + projectId + " non trouvé"));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new InvalidProjectDateException("La date de fin ne peut pas être antérieure à la date de début.");
        }

        existingProject.setName(request.getName());
        existingProject.setDescription(request.getDescription());
        existingProject.setStartDate(request.getStartDate());
        existingProject.setEndDate(request.getEndDate());
        existingProject.setUpdatedDate(LocalDate.now());

        return projectRepository.save(existingProject);
    }

    @Override
    @CheckProjectAuthorization
    public boolean deleteProject(Long projectId) {

        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet avec ID " + projectId + " non trouvé"));

        projectRepository.delete(existingProject);

        Optional<Project> projectFound = projectRepository.findById(projectId);

        return projectFound.isPresent();
    }
}