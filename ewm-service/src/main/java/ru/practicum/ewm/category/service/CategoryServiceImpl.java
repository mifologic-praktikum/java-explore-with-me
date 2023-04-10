package ru.practicum.ewm.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category with id=" + catId + " not found"));
        return CategoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        Pageable pageable = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Category> categoryList = categoryRepository.findAll(pageable);
        for (Category category : categoryList) {
            categoryDtos.add(CategoryMapper.toDto(category));
        }
        return categoryDtos;
    }

    @Transactional
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        categoryRepository.save(category);
        return CategoryMapper.toDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category categoryInStorage = CategoryMapper.toCategory(getCategoryById(catId));
        categoryInStorage.setName(categoryDto.getName());
        categoryRepository.save(categoryInStorage);
        return CategoryMapper.toDto(categoryInStorage);
    }

    @Override
    public void deleteCategory(Long catId) {
        if (eventRepository.findAllByCategoryId(catId) > 0) {
            throw new ConflictException("Can't delete category associated with events");
        }
        getCategoryById(catId);
        categoryRepository.deleteById(catId);
    }
}
