package ru.practicum.shareit.bookingTest;

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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void findAllByBookerTest() {
        User booker = userRepository.save(new User(null, "user1", "user1@gmail.com"));
        Item item1 = itemRepository.save(new Item(1L, "item1", "item1Des", true, booker, null));
        Item item2 = itemRepository.save(new Item(2L, "item2", "item2Des", true, booker, null));
        bookingRepository.save(new Booking(1L, item1, booker, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.APPROVED));
        bookingRepository.save(new Booking(2L, item2, booker, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.APPROVED));
        Pageable pageable = PageRequest.of(0, 1, Sort.by("start"));
        List<Booking> bookings = bookingRepository.findAllByBooker(booker, pageable);
        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void findAllByItemOwnerAndStatusEqualsTest() {
        User booker = userRepository.save(new User(1L, "user1", "user1@gmail.com"));
        Item item1 = itemRepository.save(new Item(1L, "item1", "item1Des", true, booker, null));
        Item item2 = itemRepository.save(new Item(2L, "item2", "item2Des", true, booker, null));
        bookingRepository.save(new Booking(1L, item1, booker, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.APPROVED));
        bookingRepository.save(new Booking(2L, item2, booker, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.APPROVED));
        Pageable pageable = PageRequest.of(0, 1, Sort.by("start"));
        List<Booking> bookings = bookingRepository.findAllByItemOwnerAndStatusEquals(booker, Status.APPROVED, pageable);
        assertThat(bookings.size()).isEqualTo(1);
    }
}