package org.example.server.service;

import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.exception.InvalidProjectDateException;
import org.example.server.exception.ProjectNotFoundException;
import org.example.server.exception.UserAlreadyAssignedException;
import org.example.server.exception.UserNotFoundException;
import org.example.server.model.Project;
import org.example.server.model.User;
import org.example.server.model.UserProject;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private UserDetails currentUser;
    private User creator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        creator = new User();
        creator.setId(1L);
        creator.setEmail("creator@example.com");

        currentUser = org.springframework.security.core.userdetails.User
                .withUsername(creator.getEmail())
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    void testCreateProject_Success() {
        ProjectDtoRequest createRequest = new ProjectDtoRequest();
        createRequest.setName("New Project");
        createRequest.setDescription("Project Description");
        createRequest.setStartDate(LocalDate.of(2023, 1, 1));
        createRequest.setEndDate(LocalDate.of(2023, 12, 31));

        Project expectedProject = new Project();
        expectedProject.setId(1L);
        expectedProject.setName(createRequest.getName());
        expectedProject.setDescription(createRequest.getDescription());
        expectedProject.setStartDate(createRequest.getStartDate());
        expectedProject.setEndDate(createRequest.getEndDate());
        expectedProject.setCreatedBy(creator);

        when(userRepository.findByEmail(currentUser.getUsername())).thenReturn(Optional.of(creator));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project savedProject = invocation.getArgument(0);
            savedProject.setId(1L);
            return savedProject;
        });

        Project createdProject = projectService.createProject(createRequest, currentUser);

        assertNotNull(createdProject);
        assertEquals("New Project", createdProject.getName());
        assertEquals("Project Description", createdProject.getDescription());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testCreateProject_InvalidDates() {
        ProjectDtoRequest createRequest = new ProjectDtoRequest();
        createRequest.setName("Invalid Project");
        createRequest.setStartDate(LocalDate.of(2023, 12, 31));
        createRequest.setEndDate(LocalDate.of(2023, 1, 1));

        Exception exception = assertThrows(InvalidProjectDateException.class, () -> {
            projectService.createProject(createRequest, currentUser);
        });

        assertEquals("La date de fin ne peut pas être antérieure à la date de début.", exception.getMessage());
        verify(projectRepository, times(0)).save(any(Project.class));
    }

    @Test
    void testCreateProject_UserNotFound() {
        ProjectDtoRequest createRequest = new ProjectDtoRequest();
        createRequest.setName("New Project");
        createRequest.setStartDate(LocalDate.of(2023, 1, 1));
        createRequest.setEndDate(LocalDate.of(2023, 12, 31));

        when(userRepository.findByEmail(currentUser.getUsername())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            projectService.createProject(createRequest, currentUser);
        });

        assertEquals("Utilisateur non trouvé", exception.getMessage());
        verify(projectRepository, times(0)).save(any(Project.class));
    }

    @Test
    void testUpdateProject_Success() {
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Old Name");
        existingProject.setDescription("Old Description");
        existingProject.setStartDate(LocalDate.of(2023, 1, 1));
        existingProject.setEndDate(LocalDate.of(2023, 12, 31));
        existingProject.setCreatedBy(creator);

        ProjectDtoRequest updateRequest = new ProjectDtoRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStartDate(LocalDate.of(2024, 1, 1));
        updateRequest.setEndDate(LocalDate.of(2024, 12, 31));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project updatedProject = projectService.updateProject(1L, updateRequest);

        assertNotNull(updatedProject);
        assertEquals("Updated Name", updatedProject.getName());
        assertEquals("Updated Description", updatedProject.getDescription());
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    void testDeleteProject_Success() {
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setCreatedBy(creator);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));

        assertDoesNotThrow(() -> projectService.deleteProject(1L));

        verify(projectRepository, times(1)).delete(existingProject);
    }

    @Test
    void testDeleteProject_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProjectNotFoundException.class, () -> {
            projectService.deleteProject(1L);
        });

        assertEquals("Projet avec ID 1 non trouvé", exception.getMessage());
    }

    @Test
    void testAddUserToProject_Success() {
        Long projectId = 1L;
        Long userId = 2L;

        Project project = new Project();
        project.setId(projectId);
        project.setUserProjects(new ArrayList<>());

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> projectService.addUserToProject(projectId, userId));

        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testAddUserToProject_UserNotFound() {
        Long projectId = 1L;
        Long userId = 2L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(new Project()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            projectService.addUserToProject(projectId, userId);
        });

        assertEquals("Utilisateur avec ID 2 non trouvé", exception.getMessage());
        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(0)).save(any(Project.class));
    }

    @Test
    void testAddUserToProject_UserAlreadyAssigned() {
        Long projectId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        UserProject userProject = UserProject.builder()
                .user(user)
                .build();

        Project project = new Project();
        project.setId(projectId);
        project.setUserProjects(Collections.singletonList(userProject));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(UserAlreadyAssignedException.class, () -> {
            projectService.addUserToProject(projectId, userId);
        });

        assertEquals("L'utilisateur est déjà assigné à ce projet.", exception.getMessage());
        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(0)).save(any(Project.class));
    }
}
