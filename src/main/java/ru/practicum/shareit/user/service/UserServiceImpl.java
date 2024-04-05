package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Validation.ValidateService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ObjectAlreadyExistsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final ValidateService validateService;

    @Override
    public User create(User user) {
        String email = user.getEmail();
        if (email == null) {
            throw new ValidationException("There is no email");
        }
        if (getAllEmail().contains(email)) {
            throw new ObjectAlreadyExistsException("Email is already exist");
        }
        validateService.validate(user);
        return userStorage.create(user);
    }

    @Override
    public User update(Long id, User user) {
        getUserById(id);
        validateService.updateValidation(user);
        String email = user.getEmail();
        if (email != null && getUserById(id).getEmail().equals(email)) {
            return userStorage.update(id, user);
        }
        if (getAllEmail().contains(user.getEmail())) {
            throw new ObjectAlreadyExistsException("Email is already exist");
        }
        return userStorage.update(id, user);
    }

    @Override
    public User getUserById(Long id) {
        if (id == null || userStorage.getById(id) == null) {
            throw new NotFoundException("User does not exist");
        }
        return userStorage.getById(id);
    }

    @Override
    public User deleteById(Long id) {
        return userStorage.deleteById(id);
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    private List<String> getAllEmail() {
        List<String> list = new ArrayList<>();
        for (User user : getUsers()) {
            list.add(user.getEmail());
        }
        return list;
    }
}
