package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public User toUser(UserCreateDto user) {
        return new User(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public User toUser(UserDto user) {
        return new User(user.getId(), user.getName(), user.getEmail());
    }

}
