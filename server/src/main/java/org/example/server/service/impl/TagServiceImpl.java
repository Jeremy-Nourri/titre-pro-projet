package org.example.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.server.aspect.CheckProjectAuthorization;
import org.example.server.dto.request.TagDtoRequest;
import org.example.server.dto.response.TagDtoResponse;
import org.example.server.exception.TagNotFoundException;
import org.example.server.exception.TaskNotFoundException;
import org.example.server.mapper.TagMapper;
import org.example.server.model.Tag;
import org.example.server.model.Task;
import org.example.server.repository.TagRepository;
import org.example.server.repository.TaskRepository;
import org.example.server.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.example.server.mapper.TagMapper.mapTagToDtoTag;

import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    @Override
    @CheckProjectAuthorization
    @Transactional
    public TagDtoResponse createTag(TagDtoRequest tagDtoRequest, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task non trouvé avec l'id : " + taskId));

        Tag tag = Tag.builder()
                .designation(tagDtoRequest.getDesignation())
                .task(task)
                .build();

        tag = tagRepository.save(tag);
        return mapTagToDtoTag(tag);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional
    public TagDtoResponse updateTag(Long taskId, Long tagId, TagDtoRequest tagDtoRequest) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag non trouvé avec l'id : " + tagId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task non trouvé avec l'id : " + taskId));

        tag.setDesignation(tagDtoRequest.getDesignation());
        tag.setTask(task);

        tag = tagRepository.save(tag);
        return mapTagToDtoTag(tag);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag non trouvé avec l'id : " + id));

        tagRepository.delete(tag);
    }

    @Override
    @CheckProjectAuthorization
    @Transactional(readOnly = true)
    public List<TagDtoResponse> getTagsByTask(Long taskId) {
        List<Tag> tags = tagRepository.findTagsByTaskId(taskId);
        return tags.stream()
                .map(TagMapper::mapTagToDtoTag)
                .collect(Collectors.toList());
    }
}
