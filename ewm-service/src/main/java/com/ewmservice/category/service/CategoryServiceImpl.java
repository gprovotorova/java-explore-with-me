package com.ewmservice.category.service;

import com.ewmservice.category.dto.CategoryDto;
import com.ewmservice.category.mapper.CategoryMapper;
import com.ewmservice.category.model.Category;
import com.ewmservice.category.repository.CategoryRepository;
import com.ewmservice.event.repository.EventRepository;
import com.ewmservice.exception.ConflictDataException;
import com.ewmservice.exception.DataViolationException;
import com.ewmservice.exception.ObjectExistException;
import com.ewmservice.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            Category category = CategoryMapper.toCategory(categoryDto);
            return CategoryMapper.toCategoryDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new ObjectExistException("Категория с именем " + categoryDto.getName() + " уже существует.");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категория с id = " + catId + " не найдена"));
        if (eventRepository.existsByCategory(category)) {
            throw new ConflictDataException("Нельзя удалить категорию. Есть связанные события.");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category savedCategory = categoryRepository.findByName(categoryDto.getName());
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категория с id = " + catId + " не найдена"));
        if (Objects.nonNull(savedCategory) && !savedCategory.getId().equals(category.getId())) {
            throw new DataViolationException("Категория уже существует");
        }
        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(Pageable page) {
        return categoryRepository.findAll(page)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категория с id = " + catId + " не найдена")));

    }
}
