package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapperImpl implements ItemMapper {
    private final RequestRepository requestRepository;

    @Override
    public Item toItem(ItemUpdateDto item, User owner) {
        if (item.getRequestId() != null) {
            return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner, requestRepository.findById(item.getRequestId()).orElse(null));
        }
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner, null);
    }

    @Override
    public Item toItem(ItemCreateDto item, User owner) {
        if (item.getRequestId() != null) {
            return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner, requestRepository.findById(item.getRequestId()).orElse(null));
        }
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner, null);
    }

    @Override
    public Item toItem(ItemDto item, User owner) {
        if (item.getRequestId() != null) {
            return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner, requestRepository.findById(item.getRequestId()).orElse(null));
        }
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner, null);
    }

    @Override
    public ItemDto toItemDto(Item item) {
        if (item.getRequest() != null) {
            return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getRequest().getId());
        }
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null);
    }

    @Override
    public List<ItemDto> toItemDtoList(List<Item> items) {
        List<ItemDto> list = new ArrayList<>();
        for (Item item : items) {
            if (item.getRequest() != null) {
                list.add(new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getRequest().getId()));
            } else {
                list.add(new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null));
            }
        }
        return list;
    }


}
