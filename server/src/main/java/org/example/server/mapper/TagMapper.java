package org.example.server.mapper;

import org.example.server.dto.response.TagDtoResponse;
import org.example.server.model.Tag;

public class TagMapper {

    public static TagDtoResponse mapTagToDtoTag(Tag tag) {
        TagDtoResponse dto = new TagDtoResponse();
        dto.setId(tag.getId());
        dto.setDesignation(tag.getDesignation());
        dto.setColor(tag.getColor());
        dto.setTaskId(tag.getTask().getId());
        dto.setCreatedDate(tag.getCreatedDate());
        dto.setUpdatedDate(tag.getUpdatedDate());

        return dto;
    }
}
