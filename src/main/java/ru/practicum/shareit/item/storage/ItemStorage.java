package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    ItemDto createItem(Item item);

    boolean isUsersItem(Item item);

    ItemDto updateItem(Item item);

    ItemDto getItemById(Long id);

    void deleteItemById(Long id);

    List<ItemDto> getItemsByUserId(Long id);

    List<ItemDto> search(String text);
}
