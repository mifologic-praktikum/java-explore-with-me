package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    public CategoryDto getCategoryById(Long catId);

    public List<CategoryDto> getCategories(int from, int size);

    public CategoryDto createCategory(CategoryDto categoryDto);

    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    public void deleteCategory(Long catId);
}
