package ru.practicum.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

@Component
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment, Event event, User user) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(EventMapper.toEventShortDto(event))
                .author(UserMapper.toUserShortDto(user))
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, Event event, User user) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .created(commentDto.getCreated())
                .updated(commentDto.getUpdated())
                .build();
    }

    public static Comment toCommentFromNew(NewCommentDto commentDto, Event event, User user) {
        return Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .build();
    }
}
