package org.example.server.service;

import org.example.server.dto.request.TagDtoRequest;
import org.example.server.dto.response.TagDtoResponse;

import java.util.List;

public interface TagService {
    TagDtoResponse createTag(TagDtoRequest tagDtoRequest, Long taskId);
    TagDtoResponse updateTag(Long taskId, Long tagId, TagDtoRequest tagDtoRequest);
    boolean deleteTag(Long id);
    List<TagDtoResponse> getTagsByTask(Long taskId);
}
