package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    ItemDto createItem(Long userId, ItemDto item);

    void saveUsersItems(Long userId, Long itemId);

    boolean isUsersItem(Long userId, Long itemId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto item);

    ItemDto getItemById(Long id);

    void deleteItemById(Long userId, Long id);

    List<ItemDto> getItemsByUserId(Long id);

    List<ItemDto> search(String text);
}
