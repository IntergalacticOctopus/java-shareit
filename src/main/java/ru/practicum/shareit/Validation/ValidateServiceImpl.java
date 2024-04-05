package ru.practicum.shareit.Validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
    public void validate(User user) {
        if (user.getName().isBlank()) {
            throw new ValidationException("Invalid user name");
        }
        String userEmail = user.getEmail();
        if (userEmail == null || !userEmail.contains("@")) {
            throw new ValidationException("Invalid user email");
        }
    }
    public void updateValidation(User user) {
        String userEmail = user.getEmail();
        if (userEmail != null) {
            if (userEmail.contains("@")) {
            } else {
                throw new ValidationException("Invalid user email");
            }
        }
    }
    public void validate(ItemDto item) {
        if (item.getName().isBlank()) {
            throw new ValidationException("Invalid item ownerId");
        }
        String description = item.getDescription();
        if (description == null) {
            throw new ValidationException("Invalid item description");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Invalid item available");
        }
    }
}
