package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(long userId, ItemCreateDto item) {
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }
        Item newItem = itemMapper.toItem(item, user);
        return itemMapper.toItemDto(itemStorage.create(newItem));
    }

    @Override
    public ItemDto update(long userId, ItemUpdateDto item) {
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }
        Long updatableItemId = item.getId();
        ItemDto updatableItem = get(updatableItemId);
        Boolean available = item.getAvailable();
        String name = item.getName();
        String description = item.getDescription();
        if (available != null) {
            updatableItem.setAvailable(available);
        }
        if (name != null) {
            updatableItem.setName(name);
        }
        if (description != null) {
            updatableItem.setDescription(description);
        }
        updatableItem.setId(updatableItemId);
        Item item1 = itemMapper.toItem(updatableItem, user);
        if (itemStorage.getItemsByUserId(userId).contains(itemStorage.get(item.getId()))) {
            return itemMapper.toItemDto(itemStorage.update(item1));
        } else {
            throw new NotFoundException("Пользователь не является владельцем товара");
        }
    }

    @Override
    public ItemDto get(long id) {
        Item item = itemStorage.get(id);
        if (item == null) {
            throw new NotFoundException("Item not found");
        }
        return itemMapper.toItemDto(item);
    }

    @Override
    public void delete(long userId, long id) {
        itemStorage.delete(id);
    }

    @Override
    public List<ItemDto> getItemsByUserId(long id) {
        if (userStorage.get(id) == null) {
            throw new NotFoundException("This user does not exist");
        }
        return itemMapper.toItemDtoList(itemStorage.getItemsByUserId(id));
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemMapper.toItemDtoList(itemStorage.search(text));
    }
}
