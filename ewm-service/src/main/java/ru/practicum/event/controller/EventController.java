package ru.practicum.event.controller;

import ru.practicum.enums.EventSort;
import ru.practicum.enums.EventState;
import ru.practicum.common.PageMaker;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {
    private final EventService eventService;
    @Value("${STATS_SERVER_URL:http://localhost:9090}")
    private String statUrl;

    private StatsClient statsClient;

    @PostConstruct
    private void init() {
        statsClient = new StatsClient(statUrl);
    }

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUserId(@PathVariable @PositiveOrZero Long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение событий, добавленных пользователем c id {}", userId);
        Pageable page = PageMaker.makePageableWithSort(from, size);
        return eventService.getEventsByUserId(userId, page);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable @PositiveOrZero Long userId,
                                    @RequestBody @Valid NewEventDto eventDto) {
        log.info("Добавление нового события {} пользователем с id {}", eventDto.toString(), userId);
        return eventService.createEvent(eventDto, userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getOneEventByUserId(@PathVariable @PositiveOrZero Long userId,
                                            @PathVariable @PositiveOrZero Long eventId) {
        log.info("Получение полной информации о событии с id {} добавленном пользователем с id {}", eventId, userId);
        return eventService.getOneEventByUserId(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable @PositiveOrZero Long userId,
                                    @PathVariable @PositiveOrZero Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest eventDto) {
        log.info("Изменение события с id {} добавленного пользователем с id {} новыми данными {}",
                eventId, userId, eventDto.toString());
        return eventService.updateEvent(userId, eventId, eventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable @PositiveOrZero Long userId,
                                                                 @PathVariable @PositiveOrZero Long eventId) {
        log.info("Получение информации о запросах на участие в событии с id {} пользователя с id {}", eventId, userId);
        return eventService.getParticipationRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateEventRequests(@PathVariable @PositiveOrZero Long userId,
                                                              @PathVariable @PositiveOrZero Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest
                                                                      request) {
        log.info("Изменение статуса (подтверждена, отменена) заявок на участие в событии пользователя с id {}", userId);
        return eventService.updateEventRequests(userId, eventId, request);
    }

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEvents(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<EventState> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                           @RequestParam(required = false) LocalDateTime rangeStart,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                           @RequestParam(required = false) LocalDateTime rangeEnd,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск событий");
        Pageable page = PageMaker.makePageableWithSort(from, size);
        return eventService.getAllEvents(users, states, categories, rangeStart, rangeEnd, page);
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateAdminEventRequests(@PathVariable @PositiveOrZero Long eventId,
                                                 @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Редактирование данных события с id {} и его статуса (отклонение/публикация)", eventId);
        return eventService.updateAdminEventRequests(eventId, updateEventAdminRequest);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEventsWithFilters(@RequestParam(required = false) String text,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) Boolean paid,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    @RequestParam(required = false) LocalDateTime rangeStart,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    @RequestParam(required = false) LocalDateTime rangeEnd,
                                                    @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                    @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(defaultValue = "10") Integer size,
                                                    HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации");

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri("/events")
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.createHit(endpointHitDto);

        Pageable page = PageMaker.makePageableWithSort(from, size);
        return eventService.getEventsWithFilters(text, categories, rangeStart, rangeEnd, onlyAvailable, paid, sort, page);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable @Positive Long id, HttpServletRequest request) {
        log.info("Получение подробной информации об опубликованном событии по его id {}", id);

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri("/events/" + id)
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.createHit(endpointHitDto);
        return eventService.getEventById(id);
    }
}
