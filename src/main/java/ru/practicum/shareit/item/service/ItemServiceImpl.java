package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemStorage itemStorage;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(long userId, ItemCreateDto item) {
        UserDto userDto = userService.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User does not exist");
        }
        User user = userMapper.toUser(userDto);
        item.setOwner(user);
        return itemStorage.createItem(itemMapper.toItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto item) {
        UserDto userDto = userService.getUserById(userId);
        Long updatableItemId = item.getId();
        ItemDto updatableItem = getItemById(updatableItemId);
        Boolean available = item.getAvailable();
        String name = item.getName();
        String description = item.getDescription();
        Boolean isRequested = item.getIsRequested();
        if (available != null) {
            updatableItem.setAvailable(available);
        }
        if (name != null) {
            updatableItem.setName(name);
        }
        if (description != null) {
            updatableItem.setDescription(description);
        }
        if (isRequested != null) {
            updatableItem.setIsRequested(isRequested);
        }
        updatableItem.setOwner(userMapper.toUser(userDto));
        updatableItem.setId(updatableItemId);
        Item item1 = itemMapper.toItem(updatableItem);
        if (itemStorage.isUsersItem(item1)) {
            return itemStorage.updateItem(item1);
        } else {
            throw new NotFoundException("Пользователь не является владельцем товара");
        }
    }

    @Override
    public ItemDto getItemById(long id) {
        ItemDto item = itemStorage.getItemById(id);
        if (item == null) {
            throw new NotFoundException("Item not found");
        }
        return item;
    }

    @Override
    public void deleteItemById(long userId, long id) {
        itemStorage.deleteItemById(id);
    }

    @Override
    public List<ItemDto> getItemsByUserId(long id) {
        if (userService.getUserById(id) == null) {
            throw new NotFoundException("This user does not exist");
        }
        return itemStorage.getItemsByUserId(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemStorage.search(text);
    }
}
