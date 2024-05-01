package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @Valid @RequestBody BookingCreateDto bookingCreateDto) throws Exception {
        log.info("Creating booking by user " + userId);
        BookingDto resultSt = bookingService.create(userId, bookingCreateDto);
        log.info("Booking created by user" + userId);
        return resultSt;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam Boolean approved) throws Exception {
        log.info("Updating booking by user " + userId);
        BookingDto result = bookingService.update(bookingId, userId, approved);
        log.info("Updated booking by user " + userId);
        return result;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting booking by user " + userId);
        BookingDto result = bookingService.getById(bookingId, userId);
        log.info("Got booking by user " + userId);
        return result;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(defaultValue = "ALL") String state) throws Exception {
        log.info("Getting booking by owner " + userId);
        List<BookingDto> result = bookingService.getBookingsByOwner(userId, state);
        log.info("Got booking by owner " + userId);
        return result;
    }

    @GetMapping
    public List<BookingDto> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") String state) throws Exception {
        log.info("Getting booking by user " + userId);
        List<BookingDto> result = bookingService.getBookingsByUser(userId, state);
        log.info("Getting booking by user " + userId);
        return result;
    }
}
