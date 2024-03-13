package ru.practicum.request.service;

import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestStatus;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.ParticipationRequestException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByUser(Long userId, Pageable page) {
        userRepository.existsById(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Событие с id = " + eventId + " не найдено."));

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ParticipationRequestException("Нельзя добавить повторный запрос.");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ParticipationRequestException("Инициатор события не может добавить запрос на участие " +
                    "в своём событии.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ParticipationRequestException("Нельзя участвовать в неопубликованном событии.");
        }
        if (event.getParticipantLimit() > 0 &&
                event.getParticipantLimit() == event.getConfirmedRequests().longValue()) {
            throw new ParticipationRequestException("Достигнут лимит запросов на участие.");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(user);
        request.setEvent(event);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        request.setCreated(LocalDateTime.now());
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.existsById(userId);
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Запрос с id = " + requestId + " не найден."));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}