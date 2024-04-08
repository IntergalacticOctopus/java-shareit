package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final ItemMapper itemMapper;
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, ArrayList<Long>> usersItems = new HashMap<>();
    private Long itemIdCounter = 0L;

    @Override
    public ItemDto createItem(Item item) {
        itemIdCounter += 1;
        item.setId(itemIdCounter);
        items.put(itemIdCounter, item);
        Long userId = item.getOwner().getId();

        ArrayList<Long> list = usersItems.get(userId);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(item.getId());
        usersItems.put(userId, list);

        return itemMapper.toItemDto(item);
    }


    @Override
    public boolean isUsersItem(Item item) {
        List<Long> list = usersItems.get(item.getOwner().getId());
        if (list != null) {
            if (list.contains(item.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemDto updateItem(Item item) {
        items.put(item.getId(), item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long id) {
        return itemMapper.toItemDto(items.get(id));
    }

    @Override
    public void deleteItemById(Long id) {
        Long userId = getItemById(id).getOwner().getId();
        ArrayList list = usersItems.get(userId);

        list.remove(list.indexOf(id));

        usersItems.put(userId, list);
        items.remove(id);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long id) {
        ArrayList<Long> idList = usersItems.get(id);
        ArrayList<ItemDto> list = new ArrayList<>();
        if (idList != null) {
            for (Long itemId : idList) {
                list.add(getItemById(itemId));
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> list = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : items.values()) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    if (item.getAvailable().equals(true)) {
                        list.add(itemMapper.toItemDto(item));
                    }
                }
            }
        }
        return list;
    }


}
