package ru.practicum.shareit.user.storage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.Map;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private Long userIdCounter = 0L;

    @Override
    public User create(User user) {
        userIdCounter += 1;
        user.setId(userIdCounter);
        userStorage.put(userIdCounter, user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        User dbUser = getById(id);
        String newUserEmail = user.getEmail();
        String newUserName = user.getName();
        if (newUserEmail != null) {
            dbUser.setEmail(newUserEmail);
        }
        if (newUserName != null) {
            dbUser.setName(newUserName);
        }
        userStorage.put(id, dbUser);
        return dbUser;
    }

    @Override
    public User getById(Long id) {
        return userStorage.get(id);
    }

    @Override
    public User deleteById(Long id) {
        return userStorage.remove(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userStorage.values());
    }
}
