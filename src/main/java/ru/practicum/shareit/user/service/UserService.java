package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserDto create(UserCreateDto user);

    UserDto update(UserUpdateDto user);

    UserDto getUserById(Long id);

    void deleteById(Long id);

    List<UserDto> getUsers();
}
