package ru.practicum.shareit.Validation;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

public interface ValidateService {
    public void validate(User user);

    public void updateValidation(User user);

    public void validate(ItemDto item);
}
