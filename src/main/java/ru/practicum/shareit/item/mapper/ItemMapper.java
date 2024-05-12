package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public interface ItemMapper {

    public Item toItem(ItemUpdateDto item, User owner);

    public Item toItem(ItemCreateDto item, User owner);

    public ItemDto toItemDto(Item item);

}
