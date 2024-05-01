package ru.practicum.shareit.item.controlleer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemCreateDto item) {
        log.info("Adding item by user" + userId);
        ItemDto addedItemDto = itemService.create(userId, item);
        log.info("Item added by user" + userId);
        return addedItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemUpdateDto item) {
        log.info("Updating item" + itemId);
        item.setId(itemId);
        ItemDto updatedItemDto = itemService.update(userId, item);
        log.info("Item updated " + updatedItemDto.getId());
        return updatedItemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Getting item " + itemId);
        ItemDto itemDto = itemService.getById(userId, itemId);
        log.info("Got item " + itemDto.getId());
        return itemDto;
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Deleting item " + itemId);
        itemService.delete(userId, itemId);
        log.info("Deleted item " + itemId);
    }

    @GetMapping
    public List<ItemDto> getUsersItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
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
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody CommentCreateDto comment,
                                 @PathVariable Long itemId) {
        log.info("Adding comment by user " + userId);
        CommentDto commentDto = itemService.addComment(itemId, userId, comment);
        log.info("Added comment by user " + userId);
        return commentDto;
    }
}
