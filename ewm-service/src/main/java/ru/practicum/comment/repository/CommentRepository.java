package ru.practicum.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c " +
            "where c.created between :rangeStart and :rangeEnd " +
            "and c.updated between :rangeStart and :rangeEnd")
    Page<Comment> getCommentsWithFilters(LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    Page<Comment> findAllByEventIdAndAuthorId(Long eventId, Long userId, Pageable page);

    Page<Comment> findAllByAuthorId(Long userId, Pageable page);

    Page<Comment> findAllByEventId(Long eventId, Pageable page);
}
