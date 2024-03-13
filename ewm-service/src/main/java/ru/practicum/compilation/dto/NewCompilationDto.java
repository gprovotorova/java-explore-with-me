package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCompilationDto {
    private Boolean pinned;

    @NotBlank(message = "Название подборки не может быть пустым")
    @Size(max = 50)
    private String title;
    private Set<Long> events;
}