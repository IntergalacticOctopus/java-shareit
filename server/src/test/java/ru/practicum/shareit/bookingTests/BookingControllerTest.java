package ru.practicum.shareit.bookingTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private ItemDto item;

    private UserDto user;
    private BookingCreateDto createBooking;
    private BookingDto booking;

    @BeforeEach
    void setUp() {
        item = new ItemDto(1L, "item1", "item1Des", true, null);
        user = new UserDto();
        createBooking = new BookingCreateDto(1L,
                LocalDateTime.of(2030, 5, 5, 5, 5, 5),
                LocalDateTime.of(2031, 5, 5, 5, 5, 5),
                item.getId());
        booking = new BookingDto(
                1L,
                LocalDateTime.of(2030, 5, 5, 5, 5, 5),
                LocalDateTime.of(2031, 5, 5, 5, 5, 5),
                item, user, Status.APPROVED);
    }

    @Test
    @SneakyThrows
    void createBookingTest() {
        when(bookingService.create(any(Long.class), any()))
                .thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(createBooking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    @SneakyThrows
    void updateBookingTest() {
        when(bookingService.update(any(Long.class), any(), any(Boolean.class)))
                .thenReturn(booking);

        mockMvc.perform(patch("/bookings/1")
                        .content(objectMapper.writeValueAsString(createBooking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1)
                        .queryParam("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    @SneakyThrows
    void getBookingByIdTest() {
        when(bookingService.getById(any(Long.class), any()))
                .thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                        .content(objectMapper.writeValueAsString(createBooking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    @SneakyThrows
    void getBookingByOwnerTest() {
        when(bookingService.getBookingsByOwner(any(Long.class), any(State.class), any()))
                .thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/owner?from=0&size=10")
                        .content(objectMapper.writeValueAsString(createBooking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(createBooking.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(createBooking.getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end", is(createBooking.getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    @SneakyThrows
    void getBookingsByUserTest() {
        when(bookingService.getBookingsByUser(any(Long.class), any(State.class), any())).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .content(objectMapper.writeValueAsString(createBooking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start",
                        is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end",
                        is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getBookingsByOwnerStateIsUnsupportedTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBookingsByOwner(1L, State.valueOfEnum("UNSUPPORTED_STATUS"),
                        any()));

        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }
}