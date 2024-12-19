package org.example.server.service;

import org.example.server.dto.request.TagDtoRequest;
import org.example.server.dto.response.TagDtoResponse;
import org.example.server.exception.TagNotFoundException;
import org.example.server.exception.TaskNotFoundException;
import org.example.server.model.PriorityEnum;
import org.example.server.model.Tag;
import org.example.server.model.Task;
import org.example.server.model.TaskStatusEnum;
import org.example.server.repository.TagRepository;
import org.example.server.repository.TaskRepository;
import org.example.server.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private Task task;
    private TagDtoRequest tagDtoRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        task = new Task();
        task.setId(1L);
        task.setTitle("Sample Task");
        task.setDetail("Tasks details");
        task.setTaskStatus(TaskStatusEnum.IN_PROGRESS);
        task.setPriority(PriorityEnum.HIGH);
        task.setDueDate(LocalDate.now().plusDays(3));
        task.setCreatedAt(LocalDate.now());
        task.setUpdatedAt(LocalDate.now());

        tag = new Tag();
        tag.setId(1L);
        tag.setDesignation("Team dev");
        tag.setTask(task);

        tagDtoRequest = new TagDtoRequest();
        tagDtoRequest.setDesignation("Updated Designation");
    }

    @Test
    void testCreateTag_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        TagDtoResponse response = tagService.createTag(tagDtoRequest, 1L);

        assertThat(response).isNotNull();
        assertThat(response.getDesignation()).isEqualTo("Team dev");
        verify(taskRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void testCreateTag_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> tagService.createTag(tagDtoRequest, 1L));
        verify(taskRepository, times(1)).findById(1L);
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void testUpdateTag_Success() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        TagDtoResponse response = tagService.updateTag(1L, 1L, tagDtoRequest);

        assertThat(response).isNotNull();
        assertThat(response.getDesignation()).isEqualTo("Updated Designation");
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    void testUpdateTag_TagNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.updateTag(1L, 1L, tagDtoRequest));
        verify(tagRepository, times(1)).findById(1L);
        verify(taskRepository, never()).findById(anyLong());
    }

    @Test
    void testDeleteTag_Success() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        doNothing().when(tagRepository).delete(tag);

        tagService.deleteTag(1L);

        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).delete(tag);
    }

    @Test
    void testDeleteTag_TagNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(1L));
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, never()).delete(any(Tag.class));
    }

    @Test
    void testGetTagsByTask_Success() {
        when(tagRepository.findTagsByTaskId(1L)).thenReturn(Collections.singletonList(tag));

        List<TagDtoResponse> responses = tagService.getTagsByTask(1L);

        assertThat(responses).isNotEmpty();
        assertThat(responses.get(0).getDesignation()).isEqualTo("Team dev");
        verify(tagRepository, times(1)).findTagsByTaskId(1L);
    }

    @Test
    void testGetTagsByTask_EmptyList() {
        when(tagRepository.findTagsByTaskId(1L)).thenReturn(Collections.emptyList());

        List<TagDtoResponse> responses = tagService.getTagsByTask(1L);

        assertThat(responses).isEmpty();
        verify(tagRepository, times(1)).findTagsByTaskId(1L);
    }
}
