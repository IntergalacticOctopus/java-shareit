package ru.practicum.shareit.requestTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {
    private static final String REQUEST_HEADER = "X-Sharer-User-Id";


    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RequestService requestService;
    @Autowired
    private MockMvc mockMvc;

    private final UserDto user = new UserDto(1L, "userDto", "user@gmail.com");
    private final RequestDto request1 = new RequestDto(1L, "request1",
            LocalDateTime.of(2000, 5, 5, 5, 5, 5), null);

    @SneakyThrows
    @Test
    void createRequestTest() {
        when(requestService.create(any())).thenReturn(request1);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(request1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request1.getDescription())))
                .andExpect(jsonPath("$.created",
                        is(request1.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @SneakyThrows
    @Test
    void getRequestByIdTest() {
        when(requestService.getById(any(Long.class), any(Long.class)))
                .thenReturn(request1);

        mockMvc.perform(get("/requests/1")
                        .content(objectMapper.writeValueAsString(request1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request1.getDescription())))
                .andExpect(jsonPath("$.created",
                        is(request1.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @SneakyThrows
    @Test
    void getAllRequestsTest() {
        when(requestService.getAllRequests(any(Long.class), any()))
                .thenReturn(List.of(request1));

        mockMvc.perform(get("/requests/all")
                        .content(objectMapper.writeValueAsString(request1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(request1.getDescription())))
                .andExpect(jsonPath("$.[0].created",
                        is(request1.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @SneakyThrows
    @Test
    void getRequestByOwnerTest() {
        when(requestService.getRequestByOwner(any(Long.class)))
                .thenReturn(List.of(request1));

        mockMvc.perform(get("/requests")
                        .content(objectMapper.writeValueAsString(request1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(REQUEST_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(request1.getDescription())))
                .andExpect(jsonPath("$.[0].created",
                        is(request1.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}