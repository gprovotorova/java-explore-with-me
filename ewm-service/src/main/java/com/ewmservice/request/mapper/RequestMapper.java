package com.ewmservice.request.mapper;

import com.ewmservice.request.dto.ParticipationRequestDto;
import com.ewmservice.request.model.ParticipationRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .created(request.getCreated())
                .build();
    }

    public static List<ParticipationRequestDto> toRequestDto(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
