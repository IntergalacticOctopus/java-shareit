package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemMapperTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ItemMapperImpl itemMapper;

    private Item item;
    private ItemDto itemDto;
    private User owner;
    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;

    @BeforeEach
    public void setUp() {
        item = new Item(1L, "item1", "itemDes", true, null, new Request());
        itemDto = itemMapper.toItemDto(item);
        owner = new User(1L, "user1", "user1@gmail.com");
        itemCreateDto = new ItemCreateDto(1L, "itemDto", "itemDtoDes", true, null, null);
        itemUpdateDto = new ItemUpdateDto(1L, "itemDto", "itemDtoDes", true, null);
    }

    @Test
    public void testCreateDtoToItem() {
        Item createdItem = itemMapper.toItem(itemCreateDto, owner);
        assertEquals(itemCreateDto.getId(), createdItem.getId());
        assertEquals(itemCreateDto.getName(), createdItem.getName());
        assertEquals(itemCreateDto.getDescription(), createdItem.getDescription());
        assertEquals(itemCreateDto.getAvailable(), createdItem.getAvailable());
        assertEquals(owner, createdItem.getOwner());
    }

    @Test
    public void testUpdateDtoToItem() {
        Item createdItem = itemMapper.toItem(itemUpdateDto, owner);
        assertEquals(itemUpdateDto.getId(), createdItem.getId());
        assertEquals(itemUpdateDto.getName(), createdItem.getName());
        assertEquals(itemUpdateDto.getDescription(), createdItem.getDescription());
        assertEquals(itemUpdateDto.getAvailable(), createdItem.getAvailable());
    }

    @Test
    public void testToItemDto() {
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertNull(itemDto.getRequestId());
    }
    @Test
    public void testToItemWithRequest() {
        itemCreateDto = new ItemCreateDto(1L, "Item Name", "Item Description", true, 1L, 1L);
        when(requestRepository.findById(itemCreateDto.getRequestId())).thenReturn(java.util.Optional.of(new Request(1L, "Request Description", owner, LocalDateTime.now())));

        Item result = itemMapper.toItem(itemCreateDto, owner);

        assertEquals(itemCreateDto.getId(), result.getId());
        assertEquals(itemCreateDto.getName(), result.getName());
        assertEquals(itemCreateDto.getDescription(), result.getDescription());
        assertEquals(itemCreateDto.getAvailable(), result.getAvailable());
        assertEquals(owner, result.getOwner());
        assertEquals(1L, result.getRequest().getId());
    }
    @Test
    public void testToItemUpdateWithRequest() {
        itemUpdateDto = new ItemUpdateDto(1L, "Updated Item Name", "Updated Item Description", true, 1L);
        when(requestRepository.findById(itemUpdateDto.getRequestId())).thenReturn(java.util.Optional.of(new Request(1L, "Request Description", owner, LocalDateTime.now())));

        Item result = itemMapper.toItem(itemUpdateDto, owner);

        assertEquals(itemUpdateDto.getId(), result.getId());
        assertEquals(itemUpdateDto.getName(), result.getName());
        assertEquals(itemUpdateDto.getDescription(), result.getDescription());
        assertEquals(itemUpdateDto.getAvailable(), result.getAvailable());
        assertEquals(owner, result.getOwner());
        assertEquals(1L, result.getRequest().getId());
    }

}