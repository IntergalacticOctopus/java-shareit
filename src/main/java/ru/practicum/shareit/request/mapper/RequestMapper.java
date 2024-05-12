package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;


@Component
@RequiredArgsConstructor
public class RequestMapper {

    private final UserMapper userMapper;

    private final UserService userService;

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public RequestDto toRequestDto(Request request) {
        RequestDto requestDto = new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                getItemsByRequestId(request.getId()));
        return requestDto;
    }

    public Request toRequest(RequestCreateDto request) {
        Request returnRequest = new Request(
                null,
                request.getDescription(),
                userMapper.toUser(userService.getUserById(request.getRequesterId())),
                request.getCreated());
        return returnRequest;
    }

    private List<ItemDto> getItemsByRequestId(Long requestId) {
        List<ItemDto> itemDtos = new ArrayList<>();
        List<Item> items = itemRepository.getItemsByRequestId(requestId, Sort.by(DESC, "id"));

        for (Item item : items) {
            ItemDto itemDto = itemMapper.toItemDto(item);
            itemDtos.add(itemDto);
        }

        return itemDtos;
    }
}
