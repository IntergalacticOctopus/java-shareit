package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemCreateDto item) {
        if (userId == null) {
            throw new NotFoundException("User does not exist");
        }
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        Item newItem = itemMapper.toItem(item, owner);
        return itemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    @Transactional
    public ItemDto update(long userId, ItemUpdateDto item) {

        Item updatebleItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new NotFoundException("Item does not exist"));
        User itemsUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        if (!updatebleItem.getOwner().getId().equals(itemsUser.getId())) {
            throw new NotFoundException("Invalid owner");
        }
        updatebleItem.setAvailable(item.getAvailable() != null ? item.getAvailable() : updatebleItem.getAvailable());
        updatebleItem.setName(item.getName() != null ? item.getName() : updatebleItem.getName());
        updatebleItem.setDescription(item.getDescription() != null ? item.getDescription() : updatebleItem.getDescription());
        return itemMapper.toItemDto(itemRepository.save(updatebleItem));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getById(Long userId, Long id) {
        Item neededItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item does not exist"));
        List<ItemDto> itemDtoList = Collections.singletonList(itemMapper.toItemDto(neededItem));
        if (neededItem.getOwner().getId().equals(userId)) {
            ItemDto.BookingDto lastBooking = bookingRepository.findFirstByItemIdInAndStartLessThanEqualAndStatus(
                            Collections.singletonList(id), LocalDateTime.now(), Status.APPROVED, Sort.by(DESC, "start"))
                    .stream()
                    .map(bookingMapper::toBookingForItemDto)
                    .findFirst()
                    .orElse(null);
            ItemDto.BookingDto nextBooking = bookingRepository.findFirstByItemIdInAndStartAfterAndStatus(
                            Collections.singletonList(id), LocalDateTime.now(), Status.APPROVED, Sort.by(ASC, "start"))
                    .stream()
                    .map(bookingMapper::toBookingForItemDto)
                    .findFirst()
                    .orElse(null);
            ItemDto itemDto = itemDtoList.get(0);
            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
        }
        ItemDto item = itemDtoList.get(0);
        item.setComments(commentRepository.getAllByItemId(id).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));

        return item;
    }

    @Override
    @Transactional
    public void delete(long userId, long id) {
        itemRepository.deleteById(id);
    }

    @Override
    @ReadOnlyProperty
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByUserId(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        List<Item> items = itemRepository.findItemByOwnerId(id);
        List<Booking> bookings = bookingRepository.findAllByItemInAndStatus(items, Status.APPROVED);
        Map<Long, List<Booking>> bookingsByItemId = new HashMap<>();
        for (Booking booking : bookings) {
            bookingsByItemId.computeIfAbsent(booking.getItem().getId(), k -> new ArrayList<>()).add(booking);
        }
        List<Comment> allComments = commentRepository.findAllByItemIn(items);
        Map<Long, List<CommentDto>> commentsByItemId = new HashMap<>();
        for (Comment comment : allComments) {
            List<CommentDto> commentDtos = commentsByItemId.computeIfAbsent(comment.getItem().getId(), k -> new ArrayList<>());
            commentDtos.add(commentMapper.toCommentDto(comment));
        }

        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = processItem(item, bookingsByItemId, commentsByItemId);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    private ItemDto processItem(Item item, Map<Long, List<Booking>> bookingsByItemId, Map<Long, List<CommentDto>> commentsByItemId) {
        List<Booking> itemBookings = bookingsByItemId.getOrDefault(item.getId(), Collections.emptyList());

        Booking nextBooking = null;
        for (Booking booking : itemBookings) {
            if (booking.getStart().isAfter(LocalDateTime.now())) {
                if (nextBooking == null || booking.getStart().isBefore(nextBooking.getStart())) {
                    nextBooking = booking;
                }
            }
        }

        Booking lastBooking = null;
        for (Booking booking : itemBookings) {
            if (booking.getStart().isBefore(LocalDateTime.now()) || booking.getStart().isEqual(LocalDateTime.now())) {
                if (lastBooking == null || booking.getStart().isAfter(lastBooking.getStart())) {
                    lastBooking = booking;
                }
            }
        }

        ItemDto itemDto = itemMapper.toItemDto(item);
        if (lastBooking != null) {
            itemDto.setLastBooking(bookingMapper.toBookingForItemDto(lastBooking));
        }
        if (nextBooking != null) {
            itemDto.setNextBooking(bookingMapper.toBookingForItemDto(nextBooking));
        }
        itemDto.setComments(commentsByItemId.getOrDefault(item.getId(), Collections.emptyList()));
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        text = text.toLowerCase();
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemRepository
                .search(text, text, true)) {
            ItemDto itemDto = itemMapper.toItemDto(item);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, Long userId, CommentCreateDto comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item does not exist"));
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                userId, itemId, Status.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new AvailableException("Access error");
        }
        Comment newComment = commentMapper.toComment(comment);
        newComment.setItem(item);
        newComment.setAuthor(user);
        newComment.setCreated(LocalDateTime.now());
        commentRepository.save(newComment);
        return commentMapper.toCommentDto(newComment);
    }
}
