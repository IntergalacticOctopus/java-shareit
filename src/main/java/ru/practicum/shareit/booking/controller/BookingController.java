package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto create(@RequestHeader(REQUEST_HEADER) long userId,
                             @Valid @RequestBody BookingCreateDto bookingCreateDto) throws Exception {
        log.info("Creating booking by user " + userId);
        BookingDto resultSt = bookingService.create(userId, bookingCreateDto);
        log.info("Booking created by user" + userId);
        return resultSt;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable long bookingId,
                             @RequestHeader(REQUEST_HEADER) long userId,
                             @RequestParam boolean approved) throws Exception {
        log.info("Updating booking by user " + userId);
        BookingDto result = bookingService.update(bookingId, userId, approved);
        log.info("Updated booking by user " + userId);
        return result;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable long bookingId,
                              @RequestHeader(REQUEST_HEADER) long userId) {
        log.info("Getting booking by user " + userId);
        BookingDto result = bookingService.getById(bookingId, userId);
        log.info("Got booking by user " + userId);
        return result;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBooking(@RequestHeader(REQUEST_HEADER) long userId,
                                       @RequestParam(defaultValue = "ALL") String state) throws Exception {
        log.info("Getting booking by owner " + userId);
        List<BookingDto> result = bookingService.getBookingsByOwner(userId, State.valueOfEnum(state));
        log.info("Got booking by owner " + userId);
        return result;
    }

    @GetMapping
    public List<BookingDto> getBookingsByUser(@RequestHeader(REQUEST_HEADER) long userId,
                                              @RequestParam(defaultValue = "ALL") String state) throws Exception {
        log.info("Getting booking by user " + userId);
        List<BookingDto> result = bookingService.getBookingsByUser(userId, State.valueOfEnum(state));
        log.info("Getting booking by user " + userId);
        return result;
    }
}
