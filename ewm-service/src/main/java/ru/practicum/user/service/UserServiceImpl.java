package ru.practicum.user.service;

import java.util.List;
import java.util.stream.Collectors;

import ru.practicum.exception.ObjectExistException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
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
