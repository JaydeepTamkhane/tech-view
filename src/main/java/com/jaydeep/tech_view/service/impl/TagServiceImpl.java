package com.jaydeep.tech_view.service.impl;

import com.jaydeep.tech_view.dto.TagDto;
import com.jaydeep.tech_view.entity.Tag;
import com.jaydeep.tech_view.exception.ResourceNotFoundException;
import com.jaydeep.tech_view.repository.TagRepository;
import com.jaydeep.tech_view.security.OwnershipUtil;
import com.jaydeep.tech_view.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final OwnershipUtil ownershipUtil;


    @Override
    @Transactional
    public TagDto createTag(TagDto tagDto) {
        ownershipUtil.ensureCurrentUserIsAdmin();
        Tag tag = modelMapper.map(tagDto, Tag.class);
        Tag savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag, TagDto.class);
    }

    @Override
    @Transactional
    public TagDto getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    @Transactional
    public TagDto updateTagById(Long id, TagDto tagDtoToUpdate) {
        ownershipUtil.ensureCurrentUserIsAdmin();
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        tag.setName(tagDtoToUpdate.getName());
        Tag updatedTag = tagRepository.save(tag);

        return modelMapper.map(updatedTag, TagDto.class);
    }

    @Override
    @Transactional
    public void deleteTagById(Long id) {
        ownershipUtil.ensureCurrentUserIsAdmin();
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        tagRepository.delete(tag);
    }

    @Override
    @Transactional
    public List<TagDto> getAllTag() {
        List<Tag> tagList = tagRepository.findAll();
        return tagList
                .stream()
                .map((element) -> modelMapper.map(element, TagDto.class))
                .collect(Collectors.toList());
    }
}
