package ru.practicum.shareit.bookingTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.Storage.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Transactional
@SpringBootTest(classes = ShareItApp.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final User user1 = new User(1L, "user1", "user1@gmail.com");
    private final User user2 = new User(2L, "user2", "user2@gmail.com");
    private final Item item1 = new Item(1L, "item1", "item1Des", true, user1, null);
    private final Booking booking = new Booking(1L, item1, user1, LocalDateTime.now(),
            LocalDateTime.now().plusHours(1), Status.WAITING);

    @BeforeEach
    void setUp() {
        bookingMapper.toBookingDto(booking);
    }

    @Test
    public void createBookingTest() {
        Long userId = 1L;
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(1L);
        BookingDto bookingDto = new BookingDto(bookingCreateDto.getId(), bookingCreateDto.getStart(),
                null, null, null, Status.WAITING);
        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(new User());
        User user = new User();
        user.setId(2L);
        item.setOwner(user);

        when(itemRepository.findById(bookingCreateDto.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingMapper.toBooking(any())).thenReturn(booking);
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDto result = bookingService.create(userId, bookingCreateDto);

        assertNotNull(result);
        assertEquals(Status.WAITING, result.getStatus());
    }

    @Test
    public void testUpdateBooking() {
        Long bookingId = 1L;
        Long userId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        Item item = new Item();
        item.setId(1L);
        User owner = new User();
        owner.setId(userId);
        item.setOwner(owner);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStatus(Status.APPROVED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto updatedBooking = bookingService.update(bookingId, userId, true);

        assertEquals(bookingDto.getId(), updatedBooking.getId());
        assertEquals(bookingDto.getStatus(), updatedBooking.getStatus());
    }


    @Test
    void getByIdTest() {
        BookingDto bookingDto = new BookingDto(booking.getId(), booking.getStart(),
                booking.getEnd(), null, null, Status.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        BookingDto returnedBooking = bookingService.getById(booking.getId(), user1.getId());

        assertThat(returnedBooking.getStart(), equalTo(booking.getStart()));
        assertThat(returnedBooking.getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void getByIncorrectIdTest() {
        assertThrows(NotFoundException.class,
                () -> bookingService.getById(9999L, 9999L));
    }

    @Test
    void gettingByBookerOrOwnerBookingTest() {
        assertThrows(NotFoundException.class,
                () -> bookingService.getById(1L, user1.getId()));
    }

    @Test
    void getBookingsByOwnerWithCURRENTStateTest() {
        when(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(any(), any(), any(), any()))
                .thenReturn(List.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        Pageable pageable = PageRequest.of(0, 20, Sort.by(DESC, "start"));
        List<BookingDto> bookings = bookingService.getBookingsByOwner(user2.getId(),
                State.CURRENT, pageable);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByOwnerWithPASTStateTest() {
        Booking booking1 = new Booking(
                1L,
                item1, user1, LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5), Status.APPROVED);
        when(bookingRepository.findAllByItemOwnerAndEndBefore(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        Pageable pageable = PageRequest.of(0, 20, Sort.by(DESC, "start"));
        List<BookingDto> bookings = bookingService.getBookingsByOwner(user2.getId(),
                State.PAST, pageable);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByOwnerWithAllStateTest() {
        Booking booking1 = new Booking(
                1L,
                item1, user1, LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5), Status.APPROVED);
        when(bookingRepository.findAllByItemOwnerAndEndBefore(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        Pageable pageable = PageRequest.of(0, 20, Sort.by(DESC, "start"));
        List<BookingDto> bookings = bookingService.getBookingsByOwner(user2.getId(),
                State.ALL, pageable);

        assertTrue(bookings.isEmpty());
    }

    @Test
    void getBookingsByOwnerWithFUTUREStateTest() {
        Booking booking1 = new Booking(
                1L,
                item1, user1, LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5), Status.APPROVED);
        when(bookingRepository.findAllByItemOwnerAndStartAfter(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        Pageable pageable = PageRequest.of(0, 20, Sort.by(DESC, "start"));
        List<BookingDto> bookings = bookingService.getBookingsByOwner(user2.getId(),
                State.FUTURE, pageable);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByOwnerWithWAITINGStateTest() {
        when(bookingRepository.findAllByItemOwnerAndStatusEquals(any(), any(), any()))
                .thenReturn(List.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        Pageable pageable = PageRequest.of(0, 20, Sort.by(DESC, "start"));

        List<BookingDto> bookings = bookingService.getBookingsByOwner(user2.getId(),
                State.WAITING, pageable);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByOwnerWithREJECTEDStateTest() {
        Booking booking1 = new Booking(
                1L, item1, user1, LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5), Status.REJECTED);
        when(bookingRepository.findAllByItemOwnerAndStatusEquals(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        Pageable pageable = PageRequest.of(0, 20, Sort.by(DESC, "start"));

        List<BookingDto> bookings = bookingService.getBookingsByOwner(user2.getId(),
                State.REJECTED, pageable);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByIncorrectUserIdTest() {
        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsByUser(9999L, State.ALL, any()));
    }

    @Test
    void getBookingsByUserWithAllStateTest() {
        when(bookingRepository.findAllByBooker(any(), any()))
                .thenReturn(List.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<BookingDto> bookings = bookingService.getBookingsByUser(user1.getId(),
                State.ALL, any());

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByUserWithCURRENTStateTest() {
        Booking booking1 = new Booking(
                1L,
                item1, user1, LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5), Status.APPROVED);
        when(bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(any(), any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<BookingDto> bookings = bookingService.getBookingsByUser(user1.getId(),
                State.CURRENT, any());

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByUserWithPASTStateTest() {
        Booking booking1 = new Booking(
                1L,
                item1, user1, LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5), Status.APPROVED);
        when(bookingRepository.findAllByBookerAndEndBefore(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<BookingDto> bookings = bookingService.getBookingsByUser(user1.getId(),
                State.PAST, any());

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByUserWithFUTUREStateTest() {
        Booking booking1 = new Booking(
                1L,
                item1, user1, LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5), Status.APPROVED);
        when(bookingRepository.findAllByBookerAndStartAfter(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<BookingDto> bookings = bookingService.getBookingsByUser(user1.getId(),
                State.FUTURE, any());

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByUserWithWAITINGStateTest() {
        when(bookingRepository.findAllByBookerAndStatusEquals(any(), any(), any()))
                .thenReturn(List.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<BookingDto> bookings = bookingService.getBookingsByUser(user1.getId(),
                State.WAITING, any());

        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByUserWithREJECTEDStateTest() {
        Booking booking1 = new Booking(
                1L,
                item1, user1, LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5), Status.REJECTED);
        when(bookingRepository.findAllByBookerAndStatusEquals(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<BookingDto> bookings = bookingService.getBookingsByUser(user1.getId(),
                State.REJECTED, any());

        assertFalse(bookings.isEmpty());
    }

    @Test
    void updateIncorrectIdTest() {
        assertThrows(NotFoundException.class,
                () -> bookingService.update(user1.getId(), 9999L, true));
    }

    @Test
    void updateAPPROVEDStatusTest() {
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(AvailableException.class,
                () -> bookingService.update(user1.getId(), 1L, true));
    }

    @Test
    void incorrectItemIdTest() {
        BookingCreateDto booking = new BookingCreateDto(1L,
                LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5),
                9999L);

        assertThrows(NotFoundException.class,
                () -> bookingService.create(user1.getId(), booking));
    }

    @Test
    void incorrectAvailableTest() {
        BookingCreateDto booking = new BookingCreateDto(1L,
                LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5),
                9999L);

        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item(1L,
                "name", "des", false, new User(), new Request())));

        assertThrows(AvailableException.class, () -> bookingService.create(user1.getId(), booking));
    }

    @Test
    void notFoundExceptionTest() {
        BookingCreateDto booking = new BookingCreateDto(1L,
                LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5),
                9999L);

        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item(1L,
                "name", "des", true, new User(), new Request())));

        assertThrows(NotFoundException.class,
                () -> bookingService.create(9999L, booking));
    }

    @Test
    void falseAvailableCreateTest() {
        item1.setAvailable(false);
        BookingCreateDto booking = new BookingCreateDto(1L,
                LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5),
                item1.getId());

        assertThrows(NotFoundException.class,
                () -> bookingService.create(user1.getId(), booking));
    }

    @Test
    void bookingByOwnerTest() {
        Item item = new Item(1L, "item1", "itemDes", false, user2, null);
        BookingCreateDto thisBooking = new BookingCreateDto(1L,
                LocalDateTime.of(2020, 5, 5, 5, 5, 5),
                LocalDateTime.of(2021, 5, 5, 5, 5, 5),
                item.getId());

        assertThrows(NotFoundException.class, () -> bookingService.create(user2.getId(), thisBooking));
    }

}
