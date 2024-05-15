package ru.practicum.shareit.itemTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.Storage.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.Storage.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private User user;
    private ItemCreateDto itemCreateDto;
    private ItemDto itemDto;
    private UserCreateDto userCreateDto;
    private CommentCreateDto commentCreateDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "item", "itemDes", true, null, null);
        itemDto = new ItemDto(1L, "itemDto", "itemDtoDes", true, null);
        itemCreateDto = new ItemCreateDto(null, "item1", "itemDes1", true, null, null);
        user = new User(1L, "user", "user@gmail.com");
        userCreateDto = new UserCreateDto(1L, "user1", "user1@gmail.com");
        commentCreateDto = new CommentCreateDto(1L, "comment", LocalDateTime.now());
        commentDto = new CommentDto(1L, "commentDto", "userName", LocalDateTime.now());
    }

    @Test
    void createItemTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemMapper.toItem(itemCreateDto, user)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.create(1L, itemCreateDto);

        assertEquals(itemDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(itemMapper, times(1)).toItem(itemCreateDto, user);
        verify(itemRepository, times(1)).save(item);
        verify(itemMapper, times(1)).toItemDto(item);
    }

    @Test
    void createIncorrectUserId() {
        assertThrows(NotFoundException.class, () -> itemService.create(null, new ItemCreateDto()));
    }

    @Test
    void updateItemTest() {
        Item oldItem = new Item(1L, "existName", "existDes", false, null, null);
        User owner = new User(1L, "owner", "owner@gmail.com");
        oldItem.setOwner(owner);
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto(1L, "newName", "newDes", true, null);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(oldItem));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemMapper.toItemDto(oldItem)).thenReturn(new ItemDto());

        itemService.update(1L, itemUpdateDto);

        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        assertEquals("newName", oldItem.getName());
        assertEquals("newDes", oldItem.getDescription());
        assertTrue(oldItem.getAvailable());
    }

    @Test
    void getItemByIdTest() {
        User owner = new User(1L, "user1", "user1@gmail.com");
        Item item = new Item(1L, "itemName", "itemDes", null, owner, null);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        ItemDto result = itemService.getById(1L, 1L);

        assertEquals(itemDto, result);
        verify(itemRepository, times(1)).findById(1L);
        verify(itemMapper, times(1)).toItemDto(item);
    }

    @Test
    void getItemsByUserIdTest() {
        User owner = new User(1L, "user1", "user1@gmail.com");
        Item item1 = new Item(1L, "itemName1", "itemDes1", null, owner, null);
        Item item2 = new Item(2L, "itemName2", "itemDes2", null, owner, null);
        List<Item> items = Arrays.asList(item1, item2);


        when(itemRepository.findItemByOwnerId(1L, any())).thenReturn(items);
        when(itemMapper.toItemDto(item1)).thenReturn(itemDto);
        when(itemMapper.toItemDto(item2)).thenReturn(itemDto);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        Pageable pageable = PageRequest.of(0, 20);
        List<ItemDto> result = itemService.getItemsByUserId(1L, pageable);

        assertEquals(2, result.size());
    }

    @Test
    void deleteByIdTest() {
        doNothing().when(itemRepository).deleteById(1L);

        itemService.delete(1L);

        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void searchTest() {
        when(itemRepository.search("name", "name", true)).thenReturn(Collections.singletonList(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        List<ItemDto> result = itemService.search("name");

        assertEquals(1, result.size());
        assertEquals(itemDto, result.get(0));
        verify(itemRepository, times(1)).search("name", "name", true);
        verify(itemMapper, times(1)).toItemDto(item);
    }

    @Test
    void searchBlankTest() {
        when(itemRepository.search("name", "name", true)).thenReturn(Collections.singletonList(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        List<ItemDto> result = itemService.search("");

        assertEquals(0, result.size());
    }

    @Test
    void createCommentTest() {
        Long userId = 1L;
        Long itemId = 2L;
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        User user = new User();
        Item item = new Item();
        Comment comment = new Comment();
        CommentDto commentDto = new CommentDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentMapper.toComment(commentCreateDto)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                any(), any(), any(), any())).thenReturn(bookings);

        CommentDto result = itemService.addComment(itemId, userId, commentCreateDto);

        assertEquals(commentDto, result);
        verify(userRepository).findById(userId);
        verify(itemRepository).findById(itemId);
        verify(commentMapper).toComment(commentCreateDto);
        verify(commentRepository).save(any(Comment.class));
        verify(commentMapper).toCommentDto(comment);
    }

    @Test
    void createIncorrectItemIdTest() {
        assertThrows(NotFoundException.class, () -> itemService.addComment(9999L, 1L, commentCreateDto));
    }

    @Test
    void createIncorrectCommentIdTest() {
        assertThrows(NotFoundException.class, () -> itemService.addComment(1L, 9999L, commentCreateDto));
    }


    @Test
    void createIncorrectIdWithExceptionTest() {
        assertThrows(NotFoundException.class, () -> itemService.create(9999L, itemCreateDto));
    }

    @Test
    void updateIncorrectUserIdTest() {
        assertThrows(NotFoundException.class, () -> itemService.update(9999L, new ItemUpdateDto()));
    }

    @Test
    void createCommentWithAvailableExceptionTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentMapper.toComment(commentCreateDto)).thenReturn(new Comment());
        when(commentRepository.save(any(Comment.class))).thenThrow(new AvailableException("Access error"));

        assertThrows(AvailableException.class, () -> itemService.addComment(1L, 1L, commentCreateDto));

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void createCommentAvailableExceptionTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                1L, 1L, Status.APPROVED, LocalDateTime.now())).thenReturn(Collections.emptyList());

        assertThrows(AvailableException.class, () -> itemService.addComment(1L, 1L, commentCreateDto));
    }

    @Test
    void createCommentWithNotFoundExceptionTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addComment(1L, 1L, commentCreateDto));
    }
}