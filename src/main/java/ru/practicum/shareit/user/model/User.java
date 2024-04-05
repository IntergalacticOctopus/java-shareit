package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@Component
public class User {
    private Long id;
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
