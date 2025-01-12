package org.example.server.service;

import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.exception.*;
import org.example.server.mapper.ProjectMapper;
import org.example.server.model.*;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.UserProjectRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProjectRepository userProjectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Spy
    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void createProject_Success() {

        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end   = LocalDate.of(2023, 12, 31);

        ProjectDtoRequest request = new ProjectDtoRequest();
        request.setName("Nouveau projet");
        request.setDescription("Description");
        request.setStartDate(start);
        request.setEndDate(end);

        UserDetails currentUser = mock(UserDetails.class);
        when(currentUser.getUsername()).thenReturn("admin@example.com");

        User user = new User();
        user.setId(100L);
        user.setEmail("admin@example.com");

        Project savedProject = Project.builder()
                .id(1L)
                .name("Nouveau projet")
                .description("Description")
                .startDate(start)
                .endDate(end)
                .createdBy(user)
                .createdDate(LocalDate.now())
                .build();

        ProjectDtoResponse dtoResponse = new ProjectDtoResponse();
        dtoResponse.setId(1L);
        dtoResponse.setName("Nouveau projet");
        dtoResponse.setDescription("Description");
        dtoResponse.setStartDate(start);
        dtoResponse.setEndDate(end);

        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(user));

        when(projectRepository.save(any(Project.class)))
                .thenReturn(savedProject);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(savedProject));

        when(projectMapper.projectToProjectDtoResponse(any(Project.class)))
                .thenReturn(dtoResponse);

        ProjectDtoResponse result = projectService.createProject(request, currentUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nouveau projet", result.getName());
        assertEquals("Description", result.getDescription());

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, atLeast(2)).findByEmail("admin@example.com");
        verify(projectMapper, times(1)).projectToProjectDtoResponse(any(Project.class));
    }

    @Test
    void createProject_InvalidDates_ThrowsException() {
        ProjectDtoRequest request = new ProjectDtoRequest();
        request.setStartDate(LocalDate.now().plusDays(10));
        request.setEndDate(LocalDate.now());

        assertThrows(InvalidProjectDateException.class, () -> {
            projectService.createProject(request, null);
        });
    }

    @Test
    void getProjectsByUserId_Success() {
        List<Project> projects = List.of(new Project());
        when(projectRepository.findAllByUserId(anyLong())).thenReturn(projects);
        when(projectMapper.projectToProjectDtoResponse(any(Project.class))).thenReturn(new ProjectDtoResponse());

        List<ProjectDtoResponse> response = projectService.getProjectsByUserId(1L);

        assertFalse(response.isEmpty());
        verify(projectRepository, times(1)).findAllByUserId(anyLong());
    }

    @Test
    void getProjectsByUserId_UserNotFound_ThrowsException() {
        when(projectRepository.findAllByUserId(anyLong())).thenReturn(List.of());

        assertThrows(UserNotFoundException.class, () -> {
            projectService.getProjectsByUserId(1L);
        });
    }

    @Test
    void updateProject_Success() {
        ProjectDtoRequest request = new ProjectDtoRequest();
        request.setName("Updated Project");

        Project existingProject = new Project();
        existingProject.setId(1L);

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(existingProject);
        when(projectMapper.projectToProjectDtoResponse(existingProject)).thenReturn(new ProjectDtoResponse());

        ProjectDtoResponse response = projectService.updateProject(1L, request);

        assertNotNull(response);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void updateProject_NotFound_ThrowsException() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.updateProject(1L, new ProjectDtoRequest());
        });
    }

    @Test
    void deleteProject_Success() {
        Project existingProject = new Project();
        existingProject.setId(1L);

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(existingProject));

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).delete(any(Project.class));
    }

    @Test
    void deleteProject_NotFound_ThrowsException() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.deleteProject(1L);
        });
    }

    @Test
    void addUserToProject_Success() {
        Project project = new Project();
        User user = new User();

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userProjectRepository.findByUserAndProject(any(User.class), any(Project.class))).thenReturn(Optional.empty());

        projectService.addUserToProject(1L, "user@example.com", RoleEnum.ADMIN);

        verify(userProjectRepository, times(1)).save(any(UserProject.class));
    }

    @Test
    void addUserToProject_AlreadyAssigned_ThrowsException() {
        Project project = new Project();
        User user = new User();

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userProjectRepository.findByUserAndProject(any(User.class), any(Project.class))).thenReturn(Optional.of(new UserProject()));

        assertThrows(UserAlreadyAssignedException.class, () -> {
            projectService.addUserToProject(1L, "user@example.com", RoleEnum.ADMIN);
        });
    }

    @Test
    void getProjectById_Success() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findByIdWithColumns(1L)).thenReturn(Optional.of(project));
        when(projectMapper.projectToProjectDtoResponse(project)).thenReturn(new ProjectDtoResponse());

        ProjectDtoResponse response = projectService.getProjectById(1L);

        assertNotNull(response);
        verify(projectRepository, times(1)).findByIdWithColumns(1L);
    }


    @Test
    void getProjectById_NotFound_ThrowsException() {
        when(projectRepository.findByIdWithColumns(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.getProjectById(1L);
        });
    }
}
