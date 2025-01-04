package org.example.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.server.aspect.CheckProjectAuthorization;
import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.exception.*;
import org.example.server.mapper.ProjectMapper;
import org.example.server.model.Project;
import org.example.server.model.RoleEnum;
import org.example.server.model.User;
import org.example.server.model.UserProject;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.UserProjectRepository;
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
    private final UserProjectRepository userProjectRepository;

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

        assignRoleToUserInProject(project.getId(), currentUser.getUsername(), RoleEnum.ADMIN);

        return ProjectMapper.ProjectToProjectDtoResponse(savedProject);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN, RoleEnum.MEMBER}, isNeedWriteAccess = false)
    @Transactional(readOnly = true)
    public List<ProjectDtoResponse> getProjectsByUserId(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec ID " + userId + " non trouvé"));

        List<Project> projects = projectRepository.findAllByUserId(userId);

        return projects.stream().map(ProjectMapper::ProjectToProjectDtoResponse).toList();
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
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
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public void deleteProject(Long projectId) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet avec ID " + projectId + " non trouvé"));

        projectRepository.delete(existingProject);
    }


    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public void addUserToProject(Long projectId, String userEmail, RoleEnum role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet non trouvé avec ID : " + projectId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'email : " + userEmail));

        if (userProjectRepository.findByUserAndProject(user, project).isPresent()) {
            throw new UserAlreadyAssignedException("Cet utilisateur est déjà associé à ce projet.");
        }

        UserProject userProject = UserProject.builder()
                .user(user)
                .project(project)
                .role(role)
                .userAddAt(LocalDate.now())
                .build();

            userProjectRepository.save(userProject);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public void assignRoleToUserInProject(Long projectId, String userEmail, RoleEnum role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet non trouvé avec ID : " + projectId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'email : " + userEmail));

        UserProject userProject = UserProject.builder()
                .user(user)
                .project(project)
                .role(role)
                .userAddAt(LocalDate.now())
                .build();

        userProjectRepository.save(userProject);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN, RoleEnum.MEMBER}, isNeedWriteAccess = true)
    @Transactional(readOnly = true)
    public ProjectDtoResponse getProjectById(Long projectId) {
        Project projectFound =  projectRepository.findByIdWithColumns(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projet avec ID " + projectId + " non trouvé"));
        return ProjectMapper.ProjectToProjectDtoResponse(projectFound);
    }
}
