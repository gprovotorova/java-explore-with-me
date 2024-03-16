package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAll(Pageable page);

    Category findByName(String name);

    @Query("select count(e.id) > 0 from Category c " +
            "left join Event e " +
            "on c.id = e.category.id " +
            "where c.id = :catId " +
            "group by c.id")
    Boolean findCategoryWithEvent(Long catId);
}



