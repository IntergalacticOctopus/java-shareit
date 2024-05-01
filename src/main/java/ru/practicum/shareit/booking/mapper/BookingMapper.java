package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@RequiredArgsConstructor
@Component
public class BookingMapper {

    private final ItemMapper itemMapper;

    private final UserMapper userMapper;

    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), itemMapper.toItemDto(booking.getItem()), userMapper.toUserDto(booking.getBooker()), booking.getStatus());
        return bookingDto;
    }

    public Booking toBooking(BookingCreateDto bookingDto) {
        Booking booking = new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd());
        return booking;
    }

    public ItemBookingDto toBookingForItemDto(Booking booking) {
        ItemBookingDto itemBookingDto = new ItemBookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem().getId(), booking.getBooker().getId());
        return itemBookingDto;
    }
}
