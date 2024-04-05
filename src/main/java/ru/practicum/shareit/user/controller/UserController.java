package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("Getting user " + id);
        User user = userService.getUserById(id);
        log.info("Got user " + user);
        return user;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user " + user);
        User createdUser = userService.create(user);
        log.info(createdUser + " user was created");
        return createdUser;
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable Long id, @Valid @RequestBody User user) {
        log.info("updating user " + user + " with id = " + id);
        User updatedUser = userService.update(id, user);
        log.info(updatedUser + " user " + id + " was updated");
        return updatedUser;
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Deleting user " + id);
        User user = userService.deleteById(id);
        log.info("User " + user + " deleted");
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Get all users");
        List<User> list = userService.getUsers();
        log.info("Get all users " + list);
        return list;
    }
}
