package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    List<CategoryDto> getAllCategories(Pageable page);

    CategoryDto getCategoryById(Long catId);
}
