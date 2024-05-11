package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Storage.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RequestRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;

    @Test
    void findAllByRequesterIdTest() {
        User user1 = new User(1L, "user1", "user1@yandex.ru");
        User user2 = new User(2L, "user2", "user2@yandex.ru");
        Item item1 = new Item(1L, "item1", "item2", true, user1, null);
        Request request2 = new Request(1L, "dis", user1, LocalDateTime.now());
        Booking booking = new Booking(1L, item1, user2, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.APPROVED);

        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        bookingRepository.save(booking);

        User requester = userRepository.save(user1);
        Request request = requestRepository.save(request2);
        Sort sort = Sort.by(Sort.Direction.ASC, "created");

        List<Request> requests = requestRepository.findAllByRequesterId(requester.getId(), sort);

        assertThat(requests).isNotEmpty();
        assertThat(requests.get(0).getId()).isEqualTo(request.getId());
        assertThat(requests.get(0).getRequester().getId()).isEqualTo(requester.getId());
    }

    @Test
    void findAllByRequesterIdNotTest() {
        User user1 = new User(null, "user1", "user3@yandex.ru");
        User user2 = new User(null, "user2", "user4@yandex.ru");
        Item item = new Item(null, "item2", "item2", true, user1, null);
        Request request2 = new Request(null, "dis", user1, LocalDateTime.now());
        Booking booking = new Booking(1L, item, user2, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.APPROVED);

        User requester = userRepository.save(user1);
        User otherUser = userRepository.save(user2);
        Request request = requestRepository.save(request2);
        Pageable pageable = PageRequest.of(0, 20);

        List<Request> requests = requestRepository.findAllByRequesterIdNot(otherUser.getId(), pageable);

        assertThat(requests).isNotEmpty();
        assertThat(requests.get(0).getId()).isEqualTo(request.getId());
        assertThat(requests.get(0).getRequester().getId()).isEqualTo(requester.getId());
    }
}
