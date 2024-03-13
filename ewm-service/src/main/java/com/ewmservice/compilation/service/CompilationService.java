package com.ewmservice.compilation.service;

import com.ewmservice.compilation.dto.CompilationDto;
import com.ewmservice.compilation.dto.NewCompilationDto;
import com.ewmservice.compilation.dto.UpdateCompilationRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompilationService {

    CompilationDto getCompilation(Long compId);

    List<CompilationDto> getAllCompilation(Boolean pinned, Pageable page);

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request);
}
