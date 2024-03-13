package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequestsByUser(Long userId, Pageable page);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
