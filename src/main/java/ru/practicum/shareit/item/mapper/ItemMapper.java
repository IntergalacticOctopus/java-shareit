package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

@Component
public interface ItemMapper {
    public Item toItem(ItemCreateDto item);

    public Item toItem(ItemUpdateDto item);

    public Item toItem(ItemDto item);

    public ItemDto toItemDto(Item item);

}
