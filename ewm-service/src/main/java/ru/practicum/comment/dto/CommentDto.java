package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.common.Constants;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String text;
    private EventShortDto event;
    private UserShortDto author;
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime created;
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime updated;
}
