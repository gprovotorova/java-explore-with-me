package ru.practicum.event.model;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.enums.EventState;
import ru.practicum.category.model.Category;
import ru.practicum.location.Location;
import ru.practicum.user.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    @ToString.Exclude
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false, referencedColumnName = "id")
    @ToString.Exclude
    @JsonProperty("initiator")
    private User initiator;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @Column(name = "views")
    private Long views;

    @Column(name = "participant_limit", nullable = false)
    private Long participantLimit;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ToString.Exclude
    private Location location;

    @ToString.Exclude
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return paid == event.paid
                && views == event.views
                && participantLimit == event.participantLimit
                && requestModeration == event.requestModeration
                && Objects.equals(id, event.id)
                && Objects.equals(title, event.title)
                && Objects.equals(annotation, event.annotation)
                && Objects.equals(eventDate, event.eventDate)
                && Objects.equals(category, event.category)
                && Objects.equals(initiator, event.initiator)
                && state == event.state
                && Objects.equals(createdOn, event.createdOn)
                && Objects.equals(publishedOn, event.publishedOn)
                && Objects.equals(confirmedRequests, event.confirmedRequests)
                && Objects.equals(description, event.description)
                && Objects.equals(location, event.location)
                && Objects.equals(compilations, event.compilations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, annotation, eventDate, paid, category, initiator, state, createdOn,
                publishedOn, confirmedRequests, views, participantLimit, requestModeration, description,
                location, compilations);
    }
}
