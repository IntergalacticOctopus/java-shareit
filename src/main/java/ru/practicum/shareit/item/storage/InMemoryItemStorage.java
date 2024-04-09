package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, ArrayList<Long>> usersItems = new HashMap<>();
    private Long itemIdCounter = 0L;

    @Override
    public Item create(Item item) {
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

        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item get(Long id) {
        return items.get(id);
    }

    @Override
    public void delete(Long id) {
        Long userId = get(id).getOwner().getId();
        ArrayList list = usersItems.get(userId);

        list.remove(list.indexOf(id));

        usersItems.put(userId, list);
        items.remove(id);
    }

    @Override
    public List<Item> getItemsByUserId(Long id) {
        ArrayList<Long> idList = usersItems.get(id);
        ArrayList<Item> list = new ArrayList<>();
        if (idList != null) {
            for (Long itemId : idList) {
                list.add(get(itemId));
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Item> search(String text) {
        List<Item> list = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : items.values()) {
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
