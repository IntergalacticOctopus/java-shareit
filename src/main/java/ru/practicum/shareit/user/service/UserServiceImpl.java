package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserCreateDto user) {
        User newUser = userMapper.toUser(user);
        return userStorage.create(newUser);
    }

    @Override
    public UserDto update(UserUpdateDto user) {
        Long userId = user.getId();
        User updataUser = getById(userId);

        String newUserEmail = user.getEmail();
        String newUserName = user.getName();
        if (newUserEmail != null) {
            updataUser.setEmail(newUserEmail);
        }
        if (newUserName != null) {
            updataUser.setName(newUserName);
        }
        return userStorage.update(updataUser);
    }

    private User getById(Long id) {
        UserDto user = getUserById(id);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }
        return userMapper.toUser(user);
    }

    @Override
    public UserDto getUserById(Long id) {

        if (id == null || !userStorage.getUsersId().contains(id)) {
            throw new NotFoundException("User does not exist");
        }
        return userStorage.getById(id);
    }

    @Override
    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return userStorage.getUsers();
    }
}
