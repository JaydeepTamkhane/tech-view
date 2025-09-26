package com.jaydeep.tech_view.controller;


import com.jaydeep.tech_view.dto.CategoryDto;
import com.jaydeep.tech_view.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDtoToUpdate) {
        return ResponseEntity.ok(categoryService.updateCategoryById(id, categoryDtoToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> categoryDtoList = categoryService.getAllCategory();

        return ResponseEntity.ok(categoryDtoList);
    }


}
