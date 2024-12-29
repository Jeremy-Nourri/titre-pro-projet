package org.example.server.service;

import org.example.server.dto.request.TagDtoRequest;
import org.example.server.dto.response.TagDtoResponse;

import java.util.List;

public interface TagService {
    TagDtoResponse createTag(Long projectId, TagDtoRequest tagDtoRequest, Long taskId);
    TagDtoResponse updateTag(Long projectId, Long taskId, Long tagId, TagDtoRequest tagDtoRequest);
    void deleteTag(Long projectId, Long taskId);
    List<TagDtoResponse> getTagsByTask(Long projectId, Long taskId);
}
