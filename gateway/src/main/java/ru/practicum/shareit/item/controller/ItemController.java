package ru.practicum.shareit.item.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@RequestMapping(value = "/items")
@Validated
@AllArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    private static final String REQUEST_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemCreateDto item,
                                         @RequestHeader(REQUEST_HEADER) long userId) {
        log.info("Adding item by user" + userId);
        ResponseEntity<Object> createdItem = itemClient.create(item, userId);
        log.info("Item added by user" + userId);
        return createdItem;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemUpdateDto item,
                                         @RequestHeader(REQUEST_HEADER) long userId,
                                         @PathVariable long itemId) {
        log.info("Updating item" + itemId);
        item.setId(itemId);
        if (item.getId() == null) {
            throw new NotFoundException("Item does not exist");
        }
        ResponseEntity<Object> updatedItemDto = itemClient.update(item, userId, itemId);
        log.info("Item updated " + itemId);
        return updatedItemDto;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable long itemId,
                                          @RequestHeader(REQUEST_HEADER) long userId) {
        log.info("Getting item " + itemId);
        ResponseEntity<Object> item = itemClient.getById(itemId, userId);
        log.info("Got item " + itemId);
        return item;
    }

    @GetMapping
    public ResponseEntity<Object> getUsersItems(@RequestHeader(REQUEST_HEADER) long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Getting items by user " + userId);
        ResponseEntity<Object> items = itemClient.getItemsByUserId(userId, from, size);
        log.info("Getting items " + items);
        return items;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Searching items by text " + text);
        ResponseEntity<Object> items = itemClient.search(text, from, size);
        log.info("Searched items by text " + text + ": " + items);
        return items;
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentCreateDto commentCreateDto,
                                             @RequestHeader(REQUEST_HEADER) long userId,
                                             @PathVariable long itemId) {
        log.info("Adding comment by user " + userId);
        ResponseEntity<Object> comment = itemClient.addComment(commentCreateDto, userId, itemId);
        log.info("Added comment by user " + userId);
        return comment;
    }

    @DeleteMapping("{itemId}")
    public ResponseEntity<Object> removeById(@PathVariable long itemId) {
        log.info("Deleting item " + itemId);
        ResponseEntity<Object> item = itemClient.delete(itemId);
        log.info("Deleted item " + itemId);
        return item;
    }
}
