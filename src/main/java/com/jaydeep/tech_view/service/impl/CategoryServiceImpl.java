package com.jaydeep.tech_view.service.impl;


import com.jaydeep.tech_view.dto.CategoryDto;
import com.jaydeep.tech_view.entity.Category;
import com.jaydeep.tech_view.exception.ResourceNotFoundException;
import com.jaydeep.tech_view.repository.CategoryRepository;
import com.jaydeep.tech_view.security.OwnershipUtil;
import com.jaydeep.tech_view.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final OwnershipUtil ownershipUtil;


    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        ownershipUtil.ensureCurrentUserIsAdmin();
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    @Transactional
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category not found with id: " + id));

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    @Transactional
    public CategoryDto updateCategoryById(Long id, CategoryDto categoryDtoToUpdate) {
        ownershipUtil.ensureCurrentUserIsAdmin();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category not found with id: " + id));
        category.setName(categoryDtoToUpdate.getName());
        Category updatedCategory = categoryRepository.save(category);

        return modelMapper.map(updatedCategory, CategoryDto.class);
    }


    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        ownershipUtil.ensureCurrentUserIsAdmin();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category not found with id: " + id));

        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public List<CategoryDto> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList
                .stream()
                .map((element) ->
                        modelMapper.map(element, CategoryDto.class))
                .collect(Collectors.toList());
    }
}
