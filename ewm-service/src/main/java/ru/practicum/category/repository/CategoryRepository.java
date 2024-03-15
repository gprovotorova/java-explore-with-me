package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAll(Pageable page);

    Category findByName(String name);

    @Query("select e from Category c " +
            "left join Event e " +
            "on c.id = e.category.id " +
            "where c.id = :catId " +
            "group by e.id, c.id " +
            "having count(e.id) > 0 ")
    Event findCategoryWithEvent(Long catId);
}



