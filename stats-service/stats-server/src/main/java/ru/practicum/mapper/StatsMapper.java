package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@Component
public class StatsMapper {

    public static EndpointHit toEndpoint(EndpointHitDto endpointDto) {
        return EndpointHit.builder()
                .id(endpointDto.getId())
                .app(endpointDto.getApp())
                .uri(endpointDto.getUri())
                .ip(endpointDto.getIp())
                .dateTime(endpointDto.getTimestamp())
                .build();
    }
}
