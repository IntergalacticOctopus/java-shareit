package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Storage.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final BookingMapper mapper;
    private static final Sort SORT = Sort.by(Sort.Direction.DESC, "start");

    @Override
    @Transactional
    public BookingDto create(Long userId, BookingCreateDto bookingCreateDto) {
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item does not exist"));
        if (!item.getAvailable()) {
            throw new AvailableException("Item does not available");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("User does not exist");
        }
        Booking newBooking = mapper.toBooking(bookingCreateDto);
        newBooking.setItem(item);
        newBooking.setBooker(user);
        newBooking.setStatus(Status.WAITING);
        Booking savedBooking = bookingRepository.save(newBooking);
        return mapper.toBookingDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking " + bookingId + " not found"));
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Data not found");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new AvailableException("Unsuitable status");
        }
        if (approved == true) {
            booking.setStatus(Status.APPROVED);
        } else if (approved == false) {
            booking.setStatus(Status.REJECTED);
        }
        booking = bookingRepository.save(booking);
        return mapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getById(Long bookingId, Long userId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new NotFoundException("Booking does not exist"));
            if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
                throw new NotFoundException("Invalid request");
            }
            return mapper.toBookingDto(booking);
        } catch (NotFoundException e) {
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByOwner(Long userId, State state, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwner(user, pageable);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByItemOwnerAndStartAfter(user, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByItemOwnerAndStatusEquals(user, Status.WAITING, pageable);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByItemOwnerAndStatusEquals(user, Status.REJECTED, pageable);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByItemOwnerAndEndBefore(user, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(user, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
                break;
            default:
                bookingList = Collections.emptyList();

        }
        return bookingList.stream().map(mapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByUser(Long userId, State state, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBooker(user, pageable);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByBookerAndStartAfter(user, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerAndStatusEquals(user, Status.WAITING, pageable);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerAndStatusEquals(user, Status.REJECTED, pageable);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByBookerAndEndBefore(user, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(user, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
                break;
            default:
                return Collections.emptyList();
        }
        return bookingList.stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }


}
