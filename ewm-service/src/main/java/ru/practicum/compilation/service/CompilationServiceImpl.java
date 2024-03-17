package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Категория с id " + compId + " не найдена."));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAllCompilation(Boolean pinned, Pageable page) {
        if (pinned == null) {
            return CompilationMapper.toCompilationDto(compilationRepository.findAll(page));
        } else if (pinned) {
            return CompilationMapper.toCompilationDto(compilationRepository.findByPinnedTrue(page));
        } else {
            return CompilationMapper.toCompilationDto(compilationRepository.findByPinnedFalse(page));
        }
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        Set<Event> events = new HashSet<>();
        Set<Long> eventsIds = compilationDto.getEvents();
        if (eventsIds != null && !eventsIds.isEmpty()) {
            events = eventRepository.findAllByIdIsIn(eventsIds);
        }
        if (compilationDto.getPinned() == null) {
            compilationDto.setPinned(false);
        }
        compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDtoWithEvents(compilation, events);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).ifPresent(category -> compilationRepository.deleteById(compId));
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Категория с id " + compId + " не найдена."));

        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getEvents() != null) {
            Set<Event> events = eventRepository.findAllById(request.getEvents())
                    .stream()
                    .collect(Collectors.toSet());
            compilation.setEvents(events);
        }
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        compilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(compilation.getEvents()
                    .stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toSet()));
        }
        return compilationDto;
    }
}
