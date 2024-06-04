package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id) {
        log.info("Getting user " + id);
        UserDto user = userService.getUserById(id);
        log.info("Got user " + id);
        return user;
    }

    @PostMapping
    public UserDto create(@RequestBody UserCreateDto user) {
        log.info("Creating user " + user);
        UserDto createdUser = userService.create(user);
        log.info("User was created " + user);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody UserUpdateDto userUpdateDto) {
        log.info("Updating user " + userUpdateDto);
        userUpdateDto.setId(id);
        UserDto updatedUserDto = userService.update(userUpdateDto);
        log.info("User updated " + userUpdateDto);
        return updatedUserDto;
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        log.info("Deleting user " + id);
        userService.deleteById(id);
        log.info("User deleted " + id);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Get all users");
        List<UserDto> list = userService.getUsers();
        log.info("Got all users " + list);
        return list;
    }
}
