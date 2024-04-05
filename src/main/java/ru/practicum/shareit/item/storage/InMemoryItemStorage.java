package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, ItemDto> items = new HashMap<>();
    private final Map<Long, ArrayList<Long>> usersItems = new HashMap<>();
    private Long itemIdCounter = 0L;

    @Override
    public ItemDto createItem(Long userId, ItemDto item) {
        itemIdCounter += 1;
        item.setId(itemIdCounter);
        items.put(itemIdCounter, item);
        ArrayList<Long> list = new ArrayList<>();
        list.add(item.getId());
        usersItems.put(userId, list);
        return items.get(itemIdCounter);
    }

    @Override
    public void saveUsersItems(Long userId, Long itemId) {
        ArrayList<Long> list = new ArrayList<>(usersItems.get(userId));
        list.add(itemId);
        usersItems.put(userId, list);
    }

    @Override
    public boolean isUsersItem(Long userId, Long itemId) {
        List<Long> list = usersItems.get(userId);
        if (list != null) {
            if (list.contains(itemId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto item) {
        ItemDto updatableItem = getItemById(itemId);
        Boolean available = item.getAvailable();
        String name = item.getName();
        String description = item.getDescription();
        Boolean isRequested = item.getIsRequested();
        if (available != null) {
            updatableItem.setAvailable(available);
        }
        if (name != null) {
            updatableItem.setName(name);
        }
        if (description != null) {
            updatableItem.setDescription(description);
        }
        if (isRequested != null) {
            updatableItem.setIsRequested(isRequested);
        }
        items.put(itemId, updatableItem);
        return updatableItem;
    }

    @Override
    public ItemDto getItemById(Long id) {
        return items.get(id);
    }

    @Override
    public void deleteItemById(Long userId, Long id) {
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
            for (ItemDto item : items.values()) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    if (item.getAvailable().equals(true)) {
                        list.add(item);
                    }
                }
            }
        }
        return list;
    }


}
