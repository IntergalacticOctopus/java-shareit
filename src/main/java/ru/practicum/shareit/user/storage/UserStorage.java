package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    UserDto create(User user);

    UserDto update(User user);

    UserDto getById(Long id);

    void deleteById(Long id);

    List<UserDto> getUsers();

    public Set<Long> getUsersId();
}
