package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapperImpl implements UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toUser(UserDto user) {
        return new User(user.getId(), user.getName(), user.getEmail());
    }
}
