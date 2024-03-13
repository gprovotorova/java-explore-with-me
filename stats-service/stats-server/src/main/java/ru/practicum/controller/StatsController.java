package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.service.StatsServiceImpl;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class StatsController {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final StatsServiceImpl statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit post(@Valid @RequestBody EndpointHitDto endpointDto) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос {}", endpointDto);
        return statsService.saveEndpoint(endpointDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsDto> get(@RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                              @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                              @RequestParam(value = "uris", required = false) List<String> uris,
                              @RequestParam(value = "unique", defaultValue = "false", required = false)
                              Boolean unique) {
        log.info("Получение статистики по посещениям со следующими параметрами: \n" +
                "время запроса от - " + start + " до - " + end + "\n" +
                "список uri - " + uris + "\n" +
                "учитываются только уникальные посещения - " + unique);
        return statsService.getStats(start, end, uris, unique);
    }
}
