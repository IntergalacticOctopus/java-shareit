package ru.practicum.shareit.item.controlleer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {


    private final ItemService itemService;
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(REQUEST_HEADER) long userId, @Valid @RequestBody ItemCreateDto item) {
        log.info("Adding item by user" + userId);
        ItemDto addedItemDto = itemService.create(userId, item);
        log.info("Item added by user" + userId);
        return addedItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(REQUEST_HEADER) long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemUpdateDto item) {
        log.info("Updating item" + itemId);
        item.setId(itemId);
        if (item.getId() == null) {
            throw new NotFoundException("Item does not exist");
        }
        ItemDto updatedItemDto = itemService.update(userId, item);
        log.info("Item updated " + updatedItemDto.getId());
        return updatedItemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(REQUEST_HEADER) long userId, @PathVariable long itemId) {
        log.info("Getting item " + itemId);
        ItemDto itemDto = itemService.getById(userId, itemId);
        log.info("Got item " + itemDto.getId());
        return itemDto;
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(@RequestHeader(REQUEST_HEADER) long userId, @PathVariable long itemId) {
        log.info("Deleting item " + itemId);
        itemService.delete(userId, itemId);
        log.info("Deleted item " + itemId);
    }

    @GetMapping
    public List<ItemDto> getUsersItems(@RequestHeader(REQUEST_HEADER) long userId) {
        log.info("Getting items by user " + userId);
        List<ItemDto> itemDtos = itemService.getItemsByUserId(userId);
        log.info("Getting items " + itemDtos);
        return itemDtos;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Searching items by text " + text);
        List<ItemDto> itemDtos = itemService.search(text);
        log.info("Searched items by text " + text + ": " + itemDtos);
        return itemDtos;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(REQUEST_HEADER) long userId,
                                 @Valid @RequestBody CommentCreateDto comment,
                                 @PathVariable long itemId) {
        log.info("Adding comment by user " + userId);
        CommentDto commentDto = itemService.addComment(itemId, userId, comment);
        log.info("Added comment by user " + userId);
        return commentDto;
    }
}
