package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(List<Long> ids, Pageable page);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long userId);
}
