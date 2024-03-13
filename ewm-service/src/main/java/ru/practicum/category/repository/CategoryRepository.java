package ru.practicum.category.repository;

import ru.practicum.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAll(Pageable page);

    Category findByName(String name);
}
