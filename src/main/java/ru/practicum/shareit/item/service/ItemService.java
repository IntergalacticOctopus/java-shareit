package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto createItem(long userId, ItemCreateDto item);

    ItemDto updateItem(long userId, ItemDto item);

    ItemDto getItemById(long id);

    void deleteItemById(long userId, long id);

    List<ItemDto> getItemsByUserId(long id);

    List<ItemDto> search(String text);
}
