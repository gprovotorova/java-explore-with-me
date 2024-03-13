package com.ewmservice.user.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ewmservice.exception.ObjectExistException;
import com.ewmservice.user.dto.UserDto;
import com.ewmservice.user.mapper.UserMapper;
import com.ewmservice.user.model.User;
import com.ewmservice.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, Pageable page) {
        List<User> users;
        if (ids.isEmpty()) {
            users = userRepository.findAll(page).toList();
        } else {
            users = userRepository.findAllByIdIn(ids, page).toList();
        }
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ObjectExistException("Пользователь с таким e-mail уже существует.");
        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresent(user -> userRepository.deleteById(userId));
    }
}
