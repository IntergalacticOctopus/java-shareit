package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        return userMapper.toUserDto(userStorage.create(userMapper.toUser(user)));
    }

    @Override
    public UserDto update(UserUpdateDto user) {
        Long userId = user.getId();
        User updataUser = userMapper.toUser(getUserById(userId));

        String newUserEmail = user.getEmail();
        String newUserName = user.getName();
        if (newUserEmail != null) {
            updataUser.setEmail(newUserEmail);
        }
        if (newUserName != null) {
            updataUser.setName(newUserName);
        }
        return userMapper.toUserDto(userStorage.update(updataUser));
    }

    @Override
    public UserDto getUserById(Long id) {
        userStorage.get(id);
        return userMapper.toUserDto(userStorage.get(id));
    }

    @Override
    public void deleteById(Long id) {
        userStorage.delete(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return userMapper.toUserDtoList(userStorage.getUsers());
    }
}
