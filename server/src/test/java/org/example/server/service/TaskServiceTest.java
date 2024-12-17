package org.example.server.service;

import org.example.server.dto.request.TaskDtoRequest;
import org.example.server.exception.ProjectNotFoundException;
import org.example.server.exception.TaskNotFoundException;
import org.example.server.model.PriorityEnum;
import org.example.server.model.Project;
import org.example.server.model.Task;
import org.example.server.model.TaskStatusEnum;
import org.example.server.repository.ProjectRepository;
import org.example.server.repository.TaskRepository;
import org.example.server.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Project mockProject;
    private Task mockTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setName("Project Test");

        mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setTitle("Test Task");
        mockTask.setDetail("Detail Task");
        mockTask.setPriority(PriorityEnum.valueOf("HIGH"));
        mockTask.setTaskStatus(TaskStatusEnum.valueOf("IN_PROGRESS"));
        mockTask.setDueDate(LocalDate.of(2024, 12, 31));
        mockTask.setProject(mockProject);
    }

    @Test
    void testCreateTask_Success() {
        TaskDtoRequest request = new TaskDtoRequest();
        request.setTitle("New Task");
        request.setDetail("Detail of new task");
        request.setPriority(PriorityEnum.valueOf("HIGH"));
        request.setTaskStatus(TaskStatusEnum.valueOf("IN_PROGRESS"));
        request.setDueDate(LocalDate.of(2024, 12, 31));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(1L);
            return savedTask;
        });

        Task createdTask = taskService.createTask(1L, request);

        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getTitle());
        assertEquals(PriorityEnum.HIGH, createdTask.getPriority());
        assertEquals(mockProject, createdTask.getProject());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_ProjectNotFound() {
        TaskDtoRequest request = new TaskDtoRequest();
        request.setTitle("New Task");

        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProjectNotFoundException.class, () -> {
            taskService.createTask(1L, request);
        });

        assertEquals("Projet non trouvé avec ID : 1", exception.getMessage());
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testUpdateTask_Success() {
        TaskDtoRequest request = new TaskDtoRequest();
        request.setTitle("Updated Task");
        request.setDetail("Updated Detail");
        request.setPriority(PriorityEnum.valueOf("LOW"));
        request.setTaskStatus(TaskStatusEnum.valueOf("COMPLETED"));
        request.setDueDate(LocalDate.of(2024, 11, 30));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updatedTask = taskService.updateTask(1L, request);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals(TaskStatusEnum.COMPLETED, updatedTask.getTaskStatus());
        verify(taskRepository, times(1)).save(mockTask);
    }

    @Test
    void testUpdateTask_NotFound() {
        TaskDtoRequest request = new TaskDtoRequest();
        request.setTitle("Updated Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(1L, request);
        });

        assertEquals("Tâche non trouvée avec ID : 1", exception.getMessage());
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testDeleteTask_Success() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(mockTask))
                .thenReturn(Optional.empty());

        boolean result = taskService.deleteTask(1L);

        assertFalse(result);
        verify(taskRepository, times(1)).delete(mockTask);
    }

    @Test
    void testGetTasksByProject_Success() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setProject(mockProject);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setProject(mockProject);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));
        when(taskRepository.findByProjectId(1L)).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getTasksByProject(1L);

        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());
        verify(taskRepository, times(1)).findByProjectId(1L);
    }

    @Test
    void testGetTasksByProject_ProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProjectNotFoundException.class, () -> {
            taskService.getTasksByProject(1L);
        });

        assertEquals("Projet non trouvé avec ID : 1", exception.getMessage());
        verify(taskRepository, times(0)).findByProjectId(anyLong());
    }

    @Test
    void testGetTaskById_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        Task task = taskService.getTaskById(1L);

        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertEquals("Tâche non trouvée avec ID : 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
    }
}
