package org.example.server.controller;

import org.example.server.dto.request.TagDtoRequest;
import org.example.server.dto.response.TagDtoResponse;
import org.example.server.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/columns/{boardColumnId}/tasks/{taskId}/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagDtoResponse> createTag(
            @PathVariable Long taskId,
            @RequestBody TagDtoRequest tagDtoRequest) {
        TagDtoResponse tagDtoResponse = tagService.createTag(tagDtoRequest, taskId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagDtoResponse);
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<TagDtoResponse> updateTag(
            @PathVariable Long taskId,
            @PathVariable Long tagId,
            @RequestBody TagDtoRequest tagDtoRequest) {
        TagDtoResponse tagDtoResponse = tagService.updateTag(taskId, tagId, tagDtoRequest);
        return ResponseEntity.ok(tagDtoResponse);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<TagDtoResponse>> getTagsByTask(
            @PathVariable Long taskId) {
        List<TagDtoResponse> tagDtoResponses = tagService.getTagsByTask(taskId);
        return ResponseEntity.ok(tagDtoResponses);
    }
}
