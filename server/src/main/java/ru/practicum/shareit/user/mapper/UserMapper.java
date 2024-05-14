package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public interface UserMapper {
    User toUser(UserCreateDto user);

    User toUser(UserDto user);

    UserDto toUserDto(User user);

}
