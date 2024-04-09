package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    Item update(Item item);

    Item get(Long id);

    void delete(Long id);

    List<Item> getItemsByUserId(Long id);

    List<Item> search(String text);
}
