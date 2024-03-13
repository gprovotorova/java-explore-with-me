package ru.practicum.event.service;

import ru.practicum.enums.AdminStateAction;
import ru.practicum.enums.EventSort;
import ru.practicum.enums.EventState;
import ru.practicum.enums.PrivateStateAction;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.service.CategoryService;
import ru.practicum.enums.RequestStatus;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.ObjectValidationException;
import ru.practicum.exception.ConflictDataException;

import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.common.Constants.MAX_DATE;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByUserId(Long userId, Pageable page) {
        return EventMapper.toEventShortDto(eventRepository.findAllByInitiatorId(userId, page));
    }

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventDto eventDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден."));

        LocalDateTime eventDate = eventDto.getEventDate();
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ObjectValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента: " + LocalDateTime.now());
        }
        Long catId = eventDto.getCategory();
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new ObjectNotFoundException("Категория с id = " + catId + " не найдена."));
        Location location = checkLocation(LocationMapper.toLocation(eventDto.getLocation()));

        Event event = EventMapper.toEvent(eventDto, user, category, location);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getOneEventByUserId(Long userId, Long eventId) {
        userRepository.existsById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Событие с id " + eventId + " " +
                        "от пользователя с id " + userId + " не найдено."));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        userRepository.existsById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("Событие с id=" + eventId + " не найдено."));

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictDataException("Изменение опубликованного события не возможно.");
        }

        String annotation = request.getAnnotation();
        if (annotation != null && !annotation.isBlank()) {
            event.setAnnotation(annotation);
        }

        if (request.getCategory() != null) {
            event.setCategory(CategoryMapper.toCategory(categoryService.getCategoryById(request.getCategory())));
        }

        String description = request.getDescription();
        if (description != null && !description.isBlank()) {
            event.setDescription(description);
        }

        LocalDateTime eventDate = request.getEventDate();
        if (eventDate != null) {
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ObjectValidationException("Дата и время на которые намечено событие не может быть " +
                        "раньше, чем через два часа от текущего момента: " + LocalDateTime.now());
            }
            event.setEventDate(eventDate);
        }

        if (request.getLocation() != null) {
            Location location = checkLocation(LocationMapper.toLocation(request.getLocation()));
            event.setLocation(location);
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        String title = request.getTitle();
        if (title != null && !title.isBlank()) {
            event.setTitle(title);
        }

        if (request.getStateAction() != null) {
            PrivateStateAction stateActionPrivate = PrivateStateAction.valueOf(request.getStateAction());
            if (stateActionPrivate.equals(PrivateStateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (stateActionPrivate.equals(PrivateStateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequest(Long userId, Long eventId) {
        userRepository.existsById(userId);
        eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() ->
                new ObjectNotFoundException("Событие с id=" + eventId + " не найдено."));
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequests(Long userId,
                                                              Long eventId,
                                                              EventRequestStatusUpdateRequest request) {
        userRepository.existsById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("Событие с id=" + eventId + " не найдено."));

        Long limit = event.getParticipantLimit() - event.getConfirmedRequests();
        if (limit == 0) {
            throw new ConflictDataException("Достигнут лимит запросов на участие.");
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequest> requestList = requestRepository.findAllByIdIn(request.getRequestIds());

        for (ParticipationRequest participationRequest : requestList) {
            if (participationRequest.getStatus() != RequestStatus.PENDING) {
                continue;
            }
            if (request.getStatus().equals(RequestStatus.CONFIRMED) && limit > 0) {
                participationRequest.setStatus(RequestStatus.CONFIRMED);
                result.getConfirmedRequests().add(RequestMapper.toParticipationRequestDto(participationRequest));
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                limit -= 1;
            } else {
                participationRequest.setStatus(RequestStatus.REJECTED);
                result.getRejectedRequests().add(RequestMapper.toParticipationRequestDto(participationRequest));
            }
        }

        eventRepository.save(event);
        requestRepository.saveAll(requestList);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEvents(List<Long> users,
                                           List<EventState> states,
                                           List<Long> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Pageable page) {
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = MAX_DATE;
        Page<Event> events = eventRepository.findAdminEvents(users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                page);

        return events.getContent().stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateAdminEventRequests(Long eventId, UpdateEventAdminRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("Событие с id=" + eventId + " не найдено."));
        if (request.getStateAction() != null) {
            AdminStateAction stateAction = AdminStateAction.valueOf(request.getStateAction());
            if (!event.getState().equals(EventState.PENDING) && stateAction.equals(AdminStateAction.PUBLISH_EVENT)) {
                throw new ConflictDataException("Событие можно публиковать, " +
                        "только если оно в состоянии ожидания публикации.");
            }
            if (event.getState().equals(EventState.PUBLISHED) && stateAction.equals(AdminStateAction.REJECT_EVENT)) {
                throw new ConflictDataException("Событие можно отклонить, только если оно еще не опубликовано.");
            }
            if (stateAction.equals(AdminStateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (stateAction.equals(AdminStateAction.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }
        String annotation = request.getAnnotation();
        if (annotation != null && !annotation.isBlank()) {
            event.setAnnotation(annotation);
        }
        if (request.getCategory() != null) {
            event.setCategory(CategoryMapper.toCategory(categoryService.getCategoryById(request.getCategory())));
        }
        String description = request.getDescription();
        if (description != null && !description.isBlank()) {
            event.setDescription(description);
        }
        LocalDateTime eventDate = request.getEventDate();
        if (eventDate != null) {
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ObjectValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                        "чем через два часа от текущего момента: " + LocalDateTime.now());
            }
            event.setEventDate(eventDate);
        }
        if (request.getLocation() != null) {
            event.setLocation(checkLocation(LocationMapper.toLocation(request.getLocation())));
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        String title = request.getTitle();
        if (title != null && !title.isBlank()) {
            event.setTitle(title);
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setViews(event.getViews() + 1);
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsWithFilters(String text,
                                                    List<Long> categories,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable,
                                                    Boolean paid,
                                                    EventSort sort,
                                                    Pageable page) {

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ObjectValidationException("Дата начала сортировки не может быть позже даты конца.");
        }

        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = MAX_DATE;

        List<EventShortDto> events = eventRepository.findPublicEvents(text,
                        categories,
                        paid,
                        onlyAvailable,
                        rangeStart,
                        rangeEnd,
                        page)
                .getContent().stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        switch (sort) {
            case EVENT_DATE:
                events.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                events.sort(Comparator.comparing(EventShortDto::getViews).reversed());
                break;
        }
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventById(Long id) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException("Событие с id = " + id + " не найдено."));

        event.setViews(event.getViews() + 1);
        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    private Location checkLocation(Location location) {
        if (locationRepository.existsByLatAndLon(location.getLat(), location.getLon())) {
            return locationRepository.findByLatAndLon(location.getLat(), location.getLon());
        } else {
            return locationRepository.save(location);
        }
    }
}
