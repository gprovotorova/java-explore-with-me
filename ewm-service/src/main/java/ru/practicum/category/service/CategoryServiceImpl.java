package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.ConflictDataException;
import ru.practicum.exception.DataViolationException;
import ru.practicum.exception.ObjectExistException;
import ru.practicum.exception.ObjectNotFoundException;
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
        if (categoryRepository.findCategoryWithEvent(catId) != null) {
            throw new ConflictDataException("Нельзя удалить категорию. Есть связанные события.");
        }
        categoryRepository.findById(catId)
                .ifPresent(category -> categoryRepository.deleteById(catId));
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
