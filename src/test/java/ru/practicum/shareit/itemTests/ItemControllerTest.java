package ru.practicum.shareit.itemTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.controlleer.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private final User user = new User(1L, "user1", "user1@gmail.com");
    private final ItemDto item = new ItemDto(2L, "item1", "item1Des", true, null);
    private final CommentDto comment = new CommentDto(4L, "comment1", user.getName(),
            LocalDateTime.now().minusYears(5));

    private static final String ITEM_URL = "/items";
    private static final String ITEM_ID = "1";
    private static final String COMMENT_URL = "/items/{itemId}/comment";
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final String USER_ID = "1";

    @Test
    @SneakyThrows
    void createItemTest() {
        when(itemService.create(any(Long.class), any()))
                .thenReturn(item);

        mvc.perform(post(ITEM_URL)
                        .content(objectMapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

    @Test
    @SneakyThrows
    void updateItemTest() {
        when(itemService.update(any(Long.class), any(ItemUpdateDto.class)))
                .thenReturn(item);

        mvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

    @Test
    @SneakyThrows
    void getItemByIdTest() {
        when(itemService.getById(any(Long.class), any(Long.class)))
                .thenReturn(item);

        mvc.perform(get("/items/1")
                        .content(objectMapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

    @Test
    @SneakyThrows
    void getItemsByUserIdTest() {
        when(itemService.getItemsByUserId(any(Long.class)))
                .thenReturn(List.of(item));

        mvc.perform(get(ITEM_URL)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(item.getName())))
                .andExpect(jsonPath("$.[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(item.getAvailable())));
    }

    @Test
    @SneakyThrows
    void deleteItemByIdTest() {
        mvc.perform(delete("/items/1")
                .header(USER_ID_HEADER, 1)).andExpect(status().isNoContent());
    }


    @SneakyThrows
    void searchTest() {
        when(itemService.search(any(String.class)))
                .thenReturn(List.of(item));

        mvc.perform(get("/items/search?text=description")
                        .content(objectMapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(item.getName())))
                .andExpect(jsonPath("$.[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(item.getAvailable())));
    }

    @Test
    @SneakyThrows
    void createCommentTest() {
        CommentDto comment = new CommentDto(4L, "comment", user.getName(), LocalDateTime.now().minusDays(30));
        when(itemService.addComment(any(Long.class), any(Long.class), any()))
                .thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())))
                .andExpect(jsonPath("$.created", is(comment.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}