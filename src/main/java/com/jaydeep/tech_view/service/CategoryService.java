package com.jaydeep.tech_view.service;

import com.jaydeep.tech_view.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(Long id);

    CategoryDto updateCategoryById(Long id, CategoryDto categoryDtoToUpdate);

    void deleteCategoryById(Long id);

    List<CategoryDto> getAllCategory();
}
