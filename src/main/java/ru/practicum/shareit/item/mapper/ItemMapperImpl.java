package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class ItemMapperImpl implements ItemMapper {
    private final RequestRepository requestRepository;

    @Override
    public Item toItem(ItemUpdateDto item, User owner) {
        Long requestId = item.getRequestId();
        Request request = requestId != null ? requestRepository.findById(requestId).orElse(null) : null;
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner, request);
    }

    @Override
    public Item toItem(ItemCreateDto item, User owner) {
        Long requestId = item.getRequestId();
        Request request = requestId != null ? requestRepository.findById(requestId).orElse(null) : null;
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), owner, request);
    }

    @Override
    public ItemDto toItemDto(Item item) {
        Long requestId = item.getRequest() != null ? item.getRequest().getId() : null;
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), requestId);
    }
}
