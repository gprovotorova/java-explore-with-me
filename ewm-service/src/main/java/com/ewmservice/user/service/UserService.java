package com.ewmservice.user.service;

import com.ewmservice.user.dto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(List<Long> ids, Pageable page);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long userId);
}
