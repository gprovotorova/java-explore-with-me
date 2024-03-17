package ru.practicum.event.dto;

import ru.practicum.common.Constants;
import ru.practicum.location.LocationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {

    @Size(min = 20, max = 2000)
    @NotBlank(message = "Аннотация события не может быть пустой.")
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20, max = 7000)
    @NotBlank(message = "Описание события не может быть пустым.")
    private String description;

    @NotNull
    @Future
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    @NotNull(message = "Локация события не может быть пустой.")
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Long participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "Название события не может быть пустым.")
    @Size(min = 3, max = 120)
    private String title;
}
