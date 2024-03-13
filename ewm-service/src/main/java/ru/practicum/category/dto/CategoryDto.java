package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым.")
    @Length(min = 2, max = 50)
    private String name;
}
