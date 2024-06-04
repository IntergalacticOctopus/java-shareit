package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private Item item2;
    private Request request;

    @BeforeEach
    void setUp() {
        user1 = new User(null, "user1", "user1@gmail.com");
        user2 = new User(null, "user2", "user2@gmail.com");
        user3 = new User(null, "user3", "user3@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        item1 = new Item(null, "item1", "itemDes", true, user1, null);
        item2 = new Item(null, "item2", "itemDes", true, user3, null);
        itemRepository.save(item1);
        itemRepository.save(item2);

        request = new Request(null, "requestDes", user2, LocalDateTime.now());
        requestRepository.save(request);
    }

    @Test
    void findItemByOwnerIdTest() {
        Pageable pageable = PageRequest.of(0, 20);
        List<Item> items = itemRepository.findItemByOwnerId(user3.getId(), pageable);

        assertThat(items).isNotNull();
        assertThat(items.get(0).getId()).isEqualTo(item2.getId());
        assertThat(items.get(0).getName()).isEqualTo(item2.getName());
        assertThat(items.get(0).getDescription()).isEqualTo(item2.getDescription());
    }

    @Test
    void getItemsByRequestIdTest() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Item> items = itemRepository.getItemsByRequestId(request.getId(), sort);

        assertThat(items).isNotNull();
    }

    @Test
    void searchTest() {
        List<Item> items = itemRepository.search("item", "itemDes", true);

        assertThat(items).isNotNull();
        assertThat(items.get(0).getId()).isEqualTo(item1.getId());
        assertThat(items.get(0).getName()).isEqualTo(item1.getName());
        assertThat(items.get(0).getDescription()).isEqualTo(item1.getDescription());
    }
}