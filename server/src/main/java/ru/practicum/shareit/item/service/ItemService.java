package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemCreateDto item);

    ItemDto update(long userId, ItemUpdateDto item);

    ItemDto getById(Long userId, Long id);

    void delete(long id);

    List<ItemDto> getItemsByUserId(long id, Pageable pageable);

    List<ItemDto> search(String text);

    CommentDto addComment(Long itemId, Long userId, CommentCreateDto comment);
}
