package com.ewmservice.event.mapper;

import com.ewmservice.category.mapper.CategoryMapper;
import com.ewmservice.category.model.Category;
import com.ewmservice.enums.EventState;
import com.ewmservice.event.dto.NewEventDto;
import com.ewmservice.event.dto.EventShortDto;
import com.ewmservice.event.dto.EventFullDto;
import com.ewmservice.event.model.Event;
import com.ewmservice.location.Location;
import com.ewmservice.location.LocationMapper;
import com.ewmservice.user.mapper.UserMapper;

import com.ewmservice.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    public static Event toEvent(NewEventDto eventDto, User user, Category category, Location location) {
        return Event.builder()
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .initiator(user)
                .category(category)
                .location(location)
                .eventDate(eventDto.getEventDate())
                .participantLimit(eventDto.getParticipantLimit() == null ? 0 : eventDto.getParticipantLimit())
                .paid(eventDto.getPaid() != null && eventDto.getPaid())
                .requestModeration(eventDto.getRequestModeration() == null || eventDto.getRequestModeration())
                .state(EventState.PENDING)
                .confirmedRequests(0L)
                .createdOn(LocalDateTime.now())
                .views(0L)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .participantLimit(event.getParticipantLimit())
                .createdOn(event.getCreatedOn() == null ? null : event.getCreatedOn())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .initiator(event.getInitiator() == null ? null : UserMapper.toUserShortDto(event.getInitiator()))
                .category(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()))
                .location(event.getLocation() == null ? null : LocationMapper.toLocationDto(event.getLocation()))
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .initiator(event.getInitiator() == null ? null : UserMapper.toUserShortDto(event.getInitiator()))
                .category(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()))
                .build();
    }

    public static List<EventShortDto> toEventShortDto(Page<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public static List<EventFullDto> toEventFullDto(Page<Event> events) {
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }
}
