package org.example.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.server.aspect.CheckProjectAuthorization;
import org.example.server.dto.request.TagDtoRequest;
import org.example.server.dto.response.TagDtoResponse;
import org.example.server.exception.InvalidTagTaskAssociationException;
import org.example.server.exception.TagNotFoundException;
import org.example.server.exception.TaskNotFoundException;
import org.example.server.mapper.TagMapper;
import org.example.server.model.RoleEnum;
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
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public TagDtoResponse createTag(Long projectId, TagDtoRequest tagDtoRequest, Long taskId) {
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
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public TagDtoResponse updateTag(Long projectId, Long taskId, Long tagId, TagDtoRequest tagDtoRequest) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag non trouvé avec l'id : " + tagId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task non trouvé avec l'id : " + taskId));

        if (!tag.getTask().getId().equals(taskId)) {
            throw new InvalidTagTaskAssociationException("Le tag avec l'id " + tagId + " n'est pas associé à la tâche avec l'id " + taskId);
        }

        if (tagDtoRequest.getDesignation() != null) {
            tag.setDesignation(tagDtoRequest.getDesignation());
        }
        if (tagDtoRequest.getColor() != null) {
            tag.setColor(tagDtoRequest.getColor());
        }
        tag.setTask(task);

        Tag updatedTag = tagRepository.save(tag);

        return mapTagToDtoTag(updatedTag);
    }


    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN}, isNeedWriteAccess = true)
    @Transactional
    public void deleteTag(Long projectId, Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag non trouvé avec l'id : " + id));

        tagRepository.delete(tag);
    }

    @Override
    @CheckProjectAuthorization(roles = {RoleEnum.ADMIN, RoleEnum.MEMBER}, isNeedWriteAccess = true)
    @Transactional(readOnly = true)
    public List<TagDtoResponse> getTagsByTask(Long projectId, Long taskId) {
        List<Tag> tags = tagRepository.findTagsByTaskId(taskId);
        return tags.stream()
                .map(TagMapper::mapTagToDtoTag)
                .collect(Collectors.toList());
    }
}
