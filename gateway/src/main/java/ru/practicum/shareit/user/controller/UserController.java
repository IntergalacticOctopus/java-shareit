package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@Validated
@AllArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get all users");
        ResponseEntity<Object> list = userClient.getUsers();
        log.info("Got all users " + list);
        return list;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable long userId) {
        log.info("Getting user " + userId);
        ResponseEntity<Object> user = userClient.getById(userId);
        log.info("Got user " + userId);
        return user;
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserCreateDto user) {
        log.info("Creating user " + user);
        ResponseEntity<Object> createdUser = userClient.create(user);
        log.info("User was created " + user);
        return createdUser;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserUpdateDto user,
                                             @PathVariable long userId) {
        log.info("Updating user " + user);
        ResponseEntity<Object> updatedUserDto = userClient.updateUser(user, userId);
        log.info("User updated " + user);
        return updatedUserDto;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> removeById(@PathVariable long userId) {
        log.info("Delete user with id {}", userId);
        return userClient.removeById(userId);
    }
}