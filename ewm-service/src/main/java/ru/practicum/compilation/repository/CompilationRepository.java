package ru.practicum.compilation.repository;

import ru.practicum.compilation.model.Compilation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Page<Compilation> findByPinnedTrue(Pageable page);

    Page<Compilation> findByPinnedFalse(Pageable page);
}
