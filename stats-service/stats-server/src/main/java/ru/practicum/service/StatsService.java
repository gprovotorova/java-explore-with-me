package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.EndpointHit;

import java.util.List;

public interface StatsService {
    EndpointHit saveEndpoint(EndpointHitDto endpointDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique);
}
