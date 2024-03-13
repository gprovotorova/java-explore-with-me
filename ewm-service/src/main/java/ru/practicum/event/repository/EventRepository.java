package ru.practicum.event.repository;

import ru.practicum.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndState(Long eventId, EventState state);

    @Query("select e from Event e " +
            "where (:text is null or (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')))) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (:onlyAvailable is null or e.participantLimit > e.confirmedRequests) " +
            "and e.eventDate between :start and :end " +
            "and e.state = 'PUBLISHED'")
    Page<Event> findPublicEvents(@Param("text") String text,
                                 @Param("categories") List<Long> categories,
                                 @Param("paid") Boolean paid,
                                 @Param("onlyAvailable") boolean onlyAvailable,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end,
                                 Pageable page);

    @Query("select e from Event e " +
            "where (:users is null or e.initiator.id in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (e.eventDate between :rangeStart and :rangeEnd)")
    Page<Event> findAdminEvents(@Param("users") List<Long> users,
                                @Param("states") List<EventState> states,
                                @Param("categories") List<Long> categories,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable page);

    @Query
    Page<Event> findAllByInitiatorId(Long userId, Pageable page);

    Set<Event> findAllByIdIsIn(Set<Long> eventsIds);

    Boolean existsByCategory(Category category);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);
}
