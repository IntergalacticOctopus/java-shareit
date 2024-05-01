package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingCreateDto bookingCreateDto) throws Exception;

    BookingDto update(Long bookingId, Long userId, Boolean approved) throws Exception;

    BookingDto getById(Long bookingId, Long userId);

    List<BookingDto> getBookingsByOwner(Long userId, String state) throws Exception;

    List<BookingDto> getBookingsByUser(Long userId, String state) throws Exception;

}
