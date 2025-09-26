package com.jaydeep.tech_view.controller;


import com.jaydeep.tech_view.dto.TagDto;
import com.jaydeep.tech_view.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto tagDto) {
        TagDto tag = tagService.createTag(tagDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tag);

    }


    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @Valid @RequestBody TagDto tagDtoToUpdate) {
        return ResponseEntity.ok(tagService.updateTagById(id, tagDtoToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTagById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    ResponseEntity<List<TagDto>> getAllTag() {
        List<TagDto> tagDtoList = tagService.getAllTag();

        return ResponseEntity.ok(tagDtoList);
    }
}
