package com.jaydeep.tech_view.service;

import com.jaydeep.tech_view.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto createTag(TagDto tagDto);

    TagDto getTagById(Long id);

    TagDto updateTagById(Long id, TagDto tagDtoToUpdate);

    void deleteTagById(Long id);

    List<TagDto> getAllTag();
}
