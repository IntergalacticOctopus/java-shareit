package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final UserMapper userMapper;
    private final Map<Long, User> userStorage = new HashMap<>();
    private Long userIdCounter = 0L;
    private Set<String> emails = new HashSet<>();

    @Override
    public UserDto create(User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new ObjectAlreadyExistsException("Email is already exist");
        }
        userIdCounter += 1;
        user.setId(userIdCounter);
        userStorage.put(userIdCounter, user);
        UserDto newUser = userMapper.toUserDto(user);
        emails.add(email);
        return newUser;
    }

    @Override
    public UserDto update(User user) {
        Long userId = user.getId();

        UserDto oldUser = userMapper.toUserDto(userStorage.get(userId));
        String oldEmail = oldUser.getEmail();

        UserDto updatedUser = userMapper.toUserDto(user);
        String newEmail = updatedUser.getEmail();
        if (!oldEmail.equals(newEmail)) {
            if (emails.contains(newEmail)) {
                throw new ObjectAlreadyExistsException("This email is already exist");
            }
            emails.remove(oldEmail);
            emails.add(newEmail);
        }
        userStorage.put(userId, user);
        return updatedUser;
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.toUserDto(userStorage.get(id));
    }

    @Override
    public void deleteById(Long id) {
        User user = userStorage.remove(id);
        emails.remove(user.getEmail());
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> list = new ArrayList<>();
        for (User user : userStorage.values()) {
            list.add(userMapper.toUserDto(user));
        }
        return list;
    }

    public Set<Long> getUsersId() {
        Set<Long> set = userStorage.keySet();
        if (set != null) {
            return set;
        } else {
            return new HashSet<>();
        }
    }
}
