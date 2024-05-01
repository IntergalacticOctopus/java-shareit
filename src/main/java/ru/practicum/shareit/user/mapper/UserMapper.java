package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public interface UserMapper {
    User toUser(UserCreateDto user);

    User toUser(UserUpdateDto user);

    User toUser(UserDto user);

    UserDto toUserDto(UserUpdateDto user);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> userList);
}
