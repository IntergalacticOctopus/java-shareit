package ru.practicum.shareit.item.controlleer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("Getting item by id " + itemId);
        ItemDto item = itemService.getItemById(itemId);
        log.info("Got item " + item);
        return item;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemCreateDto item) {
        log.info("Creating item " + item);
        ItemDto createdItem = itemService.createItem(userId, item);
        log.info("Creating item " + createdItem);
        return createdItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto item) {
        log.info("Updating item " + item + " with id=" + itemId + " by user with id=" + userId);
        item.setId(itemId);
        ItemDto updatedItem = itemService.updateItem(userId, item);
        log.info("Updated item " + updatedItem + " with id=" + itemId + " by user with id=" + userId);
        return updatedItem;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Deleting item by id " + itemId + " with ownerId=" + userId);
        itemService.deleteItemById(userId, itemId);
        log.info("Deleted item by id " + itemId + " with ownerId=" + userId);

    }

    @GetMapping
    public List<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting all items");
        List<ItemDto> list = itemService.getItemsByUserId(userId);
        log.info("Got all items list " + list);
        return list;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Searching items by text " + text);
        List<ItemDto> list = itemService.search(text);
        log.info("Searched items " + list);
        return list;
    }
}