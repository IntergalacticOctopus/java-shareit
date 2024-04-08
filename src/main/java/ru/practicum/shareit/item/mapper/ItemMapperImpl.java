package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapperImpl implements ItemMapper {
    @Override
    public Item toItem(ItemCreateDto item) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getIsRequested(), item.getOwner());
    }

    @Override
    public Item toItem(ItemUpdateDto item) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getIsRequested(), item.getOwner());
    }

    @Override
    public Item toItem(ItemDto item) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getIsRequested(), item.getOwner());
    }

    @Override
    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getIsRequested(), item.getOwner());
    }
}
