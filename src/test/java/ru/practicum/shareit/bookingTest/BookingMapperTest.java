package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTest {

    @Mock
    private ItemMapperImpl itemMapperMock;

    @Mock
    private UserMapperImpl userMapperMock;

    @InjectMocks
    private BookingMapper bookingMapper;

    @Test
    void toBookingTest() {
        BookingCreateDto bookingDto = new BookingCreateDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L);

        Booking actualBooking = bookingMapper.toBooking(bookingDto);

        assertEquals(1L, actualBooking.getId());
        assertEquals(bookingDto.getStart(), actualBooking.getStart());
        assertEquals(bookingDto.getEnd(), actualBooking.getEnd());
    }

    @Test
    void toBookingDtoTest() {
        Booking booking = new Booking(1L, new Item(), new User(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.APPROVED);
        BookingDto expectedDto = new BookingDto(1L, booking.getStart(), booking.getEnd(), new ItemDto(), new UserDto(), Status.APPROVED);

        when(itemMapperMock.toItemDto(booking.getItem())).thenReturn(new ItemDto());
        when(userMapperMock.toUserDto(booking.getBooker())).thenReturn(new UserDto());

        BookingDto actualDto = bookingMapper.toBookingDto(booking);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void toBookingForItemDtoTest() {
        Request request = new Request(1L, "requestDes", new User(), LocalDateTime.now());
        Booking booking = new Booking(1L,
                new Item(1L, "itemName", "itemDes", true, new User(), request),
                new User(1L, "userName", "user@gmail.com"), LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.APPROVED);
        ItemDto.BookingDto expectedDto = new ItemDto.BookingDto(1L, booking.getStart(), booking.getEnd(),
                1L, 1L);

        ItemDto.BookingDto actualDto = bookingMapper.toBookingForItemDto(booking);

        assertEquals(expectedDto, actualDto);
    }
}