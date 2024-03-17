package ru.practicum.common;

import ru.practicum.exception.InvalidPathVariableException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageMaker {
    public static Pageable makePageableWithSort(Integer from, Integer size) {
        if (from == null || size == null) {
            return Pageable.unpaged();
        } else if (from < 0 || size <= 0) {
            throw new InvalidPathVariableException("Неправильно заданы параметры.");
        }
        return PageRequest.of(from / size, size, Sort.Direction.ASC, "id");
    }
}
