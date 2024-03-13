package com.ewmservice.category.mapper;

import com.ewmservice.category.dto.CategoryDto;
import com.ewmservice.category.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(),
                category.getName()
        );
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}
