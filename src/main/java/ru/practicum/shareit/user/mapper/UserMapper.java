package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

@Component
public interface UserMapper {
    public User toUser(UserCreateDto user);

    public User toUser(UserUpdateDto user);

    public User toUser(UserDto user);

    public UserDto toUserDto(User user);

}
