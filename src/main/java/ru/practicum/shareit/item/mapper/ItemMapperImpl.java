package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapperImpl implements ItemMapper {

    @Override
    public Item toItem(ItemUpdateDto item, User owner) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner);
    }

    @Override
    public Item toItem(ItemCreateDto item, User owner) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner);
    }

    @Override
    public Item toItem(ItemDto item, User owner) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner);
    }

    @Override
    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    @Override
    public List<ItemDto> toItemDtoList(List<Item> items) {
        List<ItemDto> list = new ArrayList<>();
        for (Item item : items) {
            list.add(new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable()));
        }
        return list;
    }


}
