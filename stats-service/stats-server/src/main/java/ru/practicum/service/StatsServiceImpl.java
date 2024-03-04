package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.exception.InvalidPathVariableException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public EndpointHit saveEndpoint(EndpointHitDto endpointDto) {
        return statsRepository.save(StatsMapper.toEndpoint(endpointDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDate = parseDate(start);
        LocalDateTime endDate = parseDate(end);

        if (startDate.isAfter(endDate)) {
            throw new InvalidPathVariableException("Дата и время начала диапазона не может быть " +
                    "позже даты и времени конца диапазона");
        }
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository.getAllUniqueHits(startDate, endDate);
            } else {
                return statsRepository.getAllHits(startDate, endDate);
            }
        } else {
            if (unique) {
                return statsRepository.getUniqueHitsOrderByUri(startDate, endDate, uris);
            } else {
                return statsRepository.getHitsOrderByUri(startDate, endDate, uris);
            }
        }
    }

    private LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

}
