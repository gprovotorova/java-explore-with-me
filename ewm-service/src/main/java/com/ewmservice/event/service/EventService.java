package com.ewmservice.event.service;

import com.ewmservice.enums.EventSort;
import com.ewmservice.enums.EventState;
import com.ewmservice.event.dto.EventFullDto;
import com.ewmservice.event.dto.EventShortDto;
import com.ewmservice.event.dto.NewEventDto;
import com.ewmservice.event.dto.UpdateEventAdminRequest;
import com.ewmservice.event.dto.UpdateEventUserRequest;
import com.ewmservice.request.dto.EventRequestStatusUpdateRequest;
import com.ewmservice.request.dto.EventRequestStatusUpdateResult;
import com.ewmservice.request.dto.ParticipationRequestDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEventsByUserId(Long userId, Pageable page);

    EventFullDto createEvent(NewEventDto eventDto, Long userId);

    EventFullDto getOneEventByUserId(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    List<ParticipationRequestDto> getParticipationRequest(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequests(Long userId,
                                                       Long eventId,
                                                       EventRequestStatusUpdateRequest eventStatusUpdateRequest);

    List<EventFullDto> getAllEvents(List<Long> users,
                                    List<EventState> states,
                                    List<Long> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Pageable page);

    EventFullDto updateAdminEventRequests(Long eventId, UpdateEventAdminRequest request);

    List<EventShortDto> getEventsWithFilters(String text,
                                             List<Long> categories,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             Boolean onlyAvailable,
                                             Boolean paid,
                                             EventSort sort,
                                             Pageable page);

    EventFullDto getEventById(Long id);
}
