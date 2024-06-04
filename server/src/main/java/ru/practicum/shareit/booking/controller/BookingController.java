package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";
    private static final Sort SORT = Sort.by(DESC, "start");

    @PostMapping
    public BookingDto create(@RequestHeader(REQUEST_HEADER) long userId,
                             @RequestBody BookingCreateDto bookingCreateDto) {
        log.info("Creating booking by user " + userId);
        BookingDto resultSt = bookingService.create(userId, bookingCreateDto);
        log.info("Booking created by user" + userId);
        return resultSt;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable long bookingId,
                             @RequestHeader(REQUEST_HEADER) long userId,
                             @RequestParam boolean approved) {
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
                                       @RequestParam(defaultValue = "ALL") String state,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Getting booking by owner " + userId);
        Pageable pageable = PageRequest.of(from, size, SORT);
        List<BookingDto> bookingDtoList = bookingService.getBookingsByOwner(userId,
                State.valueOfEnum(state), pageable);
        log.info("Got booking by owner " + userId);
        return bookingDtoList;
    }

    @GetMapping
    public List<BookingDto> getBookingsByUser(@RequestHeader(REQUEST_HEADER) long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Getting booking by user " + userId + ", from = " + from + " and size = " + size);
        Pageable pageable = PageRequest.of((from / size), size, SORT);
        List<BookingDto> bookingDtoList = bookingService
                .getBookingsByUser(userId, State.valueOfEnum(state), pageable);
        log.info("Got booking by user " + userId);
        return bookingDtoList;
    }
}
