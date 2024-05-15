package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.ItemBookingCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(REQUEST_HEADER) long userId,
                                         @RequestBody @Valid ItemBookingCreateDto requestDto) {
        log.info("Creating booking by user " + userId);
        ResponseEntity<Object> resultSt = bookingClient.create(userId, requestDto);
        log.info("Booking created by user" + userId);
        return resultSt;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(REQUEST_HEADER) long userId,
                                         @PathVariable long bookingId, @RequestParam String approved) {
        log.info("Updating booking by user " + userId);
        ResponseEntity<Object> result = bookingClient.update(userId, bookingId, approved);
        log.info("Updated booking by user " + userId);
        return result;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(REQUEST_HEADER) long userId,
                                          @PathVariable Long bookingId) {
        log.info("Getting booking by user " + userId);
        ResponseEntity<Object> result = bookingClient.getById(userId, bookingId);
        log.info("Got booking by user " + userId);
        return result;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader(REQUEST_HEADER) long userId,
                                                    @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "10") @Positive int size) {
        BookingState bookingState = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Getting booking by owner " + userId);
        ResponseEntity<Object> bookingDtoList = bookingClient.getBookingByOwner(userId, bookingState, from, size);
        log.info("Got booking by owner " + userId);
        return bookingDtoList;
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader(REQUEST_HEADER) long userId,
                                                    @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                    @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        BookingState bookingState = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Getting booking by user " + userId + ", from = " + from + " and size = " + size);
        ResponseEntity<Object> bookingDtoList = bookingClient.getBookingsByUser(userId, bookingState, from, size);
        log.info("Got booking by user " + userId);
        return bookingDtoList;
    }
}