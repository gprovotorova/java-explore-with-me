package com.ewmservice.compilation.mapper;

import com.ewmservice.compilation.dto.CompilationDto;
import com.ewmservice.compilation.dto.NewCompilationDto;
import com.ewmservice.compilation.model.Compilation;
import com.ewmservice.event.dto.EventShortDto;
import com.ewmservice.event.mapper.EventMapper;
import com.ewmservice.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned() != null && compilationDto.getPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        Set<EventShortDto> eventsDto = new HashSet<>();
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            eventsDto = compilation.getEvents().stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toSet());
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(eventsDto)
                .build();
    }

    public static CompilationDto toCompilationDtoWithEvents(Compilation compilation, Set<Event> events) {
        Set<EventShortDto> eventsDto = events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toSet());

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(eventsDto)
                .build();
    }

    public static List<CompilationDto> toCompilationDto(Page<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
