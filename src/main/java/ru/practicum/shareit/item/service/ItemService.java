package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    public ItemDto create(long userId, ItemCreateDto item);

    ItemDto update(long userId, ItemUpdateDto item);

    ItemDto get(long id);

    void delete(long userId, long id);

    List<ItemDto> getItemsByUserId(long id);

    List<ItemDto> search(String text);
}
