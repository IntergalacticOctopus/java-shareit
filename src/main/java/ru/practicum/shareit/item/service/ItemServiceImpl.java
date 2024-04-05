package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Validation.ValidateService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemStorage itemStorage;
    private final ValidateService validateService;

    @Override
    public ItemDto createItem(Long userId, ItemDto item) {
        validateService.validate(item);
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("User does not exist");
        }
        return itemStorage.createItem(userId, item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto item) {
        userService.getUserById(userId);
        getItemById(itemId);
        if (itemStorage.isUsersItem(userId, itemId)) {
            return itemStorage.updateItem(userId, itemId, item);
        } else {
            throw new NotFoundException("Пользователь не является владельцем товара");
        }
    }

    @Override
    public ItemDto getItemById(Long id) {
        ItemDto item = itemStorage.getItemById(id);
        if (item == null) {
            throw new NotFoundException("Item not found");
        }
        return item;
    }

    @Override
    public void deleteItemById(Long userId, Long id) {
        itemStorage.deleteItemById(userId, id);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long id) {
        return itemStorage.getItemsByUserId(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemStorage.search(text);
    }
}
