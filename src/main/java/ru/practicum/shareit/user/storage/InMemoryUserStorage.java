package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectAlreadyExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private Long userIdCounter = 0L;
    private Set<String> emails = new HashSet<>();

    @Override
    public User create(User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new ObjectAlreadyExistsException("Email is already exist");
        }
        userIdCounter += 1;
        user.setId(userIdCounter);
        userStorage.put(userIdCounter, user);
        emails.add(email);
        return user;
    }

    @Override
    public User update(User user) {
        Long userId = user.getId();
        User oldUser = userStorage.get(userId);
        String oldEmail = oldUser.getEmail();
        String newEmail = user.getEmail();
        if (!oldEmail.equals(newEmail)) {
            if (emails.contains(newEmail)) {
                throw new ObjectAlreadyExistsException("This email is already exist");
            }
            emails.remove(oldEmail);
            emails.add(newEmail);
        }
        userStorage.put(userId, user);
        return user;
    }

    @Override
    public User get(Long id) {
        return userStorage.get(id);
    }

    @Override
    public void delete(Long id) {
        User user = userStorage.remove(id);
        emails.remove(user.getEmail());
    }

    @Override
    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        for (User user : userStorage.values()) {
            list.add(user);
        }
        return list;
    }
}
