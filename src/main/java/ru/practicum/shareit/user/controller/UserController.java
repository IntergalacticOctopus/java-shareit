package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        log.info("Getting user " + id);
        UserDto user = userService.getUserById(id);
        log.info("Got user " + user);
        return user;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserCreateDto user) {
        log.info("Creating user " + user);
        UserDto createdUser = userService.create(user);
        log.info(createdUser + " user was created");
        return createdUser;
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto user) {
        log.info("updating user " + user + " with id = " + id);
        user.setId(id);
        UserDto updatedUser = userService.update(user);
        log.info(updatedUser + " user " + id + " was updated");
        return updatedUser;
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Deleting user " + id);
        userService.deleteById(id);
        log.info("User " + id + " deleted");
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Get all users");
        List<UserDto> list = userService.getUsers();
        log.info("Get all users " + list);
        return list;
    }
}
