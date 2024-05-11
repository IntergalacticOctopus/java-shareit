package ru.practicum.shareit.requestTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Transactional
@SpringBootTest(classes = ShareItApp.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {

    private final RequestService requestService;
    private final UserService userService;
    private final UserRepository userRepository;

    private UserCreateDto userDto1;
    private UserCreateDto userDto2;
    private RequestCreateDto requestDto;

    @BeforeEach
    void setup() {
        userDto1 = new UserCreateDto(1L, "user", "user@gmail.com");
        userDto2 = new UserCreateDto(2L, "user1", "user1@gmail.com");
        requestDto = new RequestCreateDto(5L, "request", 1L,
                LocalDateTime.of(2022, 10, 12, 21, 40, 0));
    }

    @Test
    @Transactional
    void createRequestTest() {
        UserDto createdUser = userService.create(userDto2);
        requestDto.setRequesterId(createdUser.getId());
        RequestDto createdRequest = requestService.create(requestDto);

        assertThat(createdRequest.getDescription(), equalTo(requestDto.getDescription()));
    }

    @Test
    void createIncorrectRequestIdTest() {
        requestDto.setRequesterId(500L);
        assertThrows(NotFoundException.class,
                () -> requestService.create(requestDto));
    }

    @Test
    @Transactional
    void getRequestByIdTest() {
        UserDto user = userService.create(userDto1);
        requestDto.setRequesterId(user.getId());
        requestDto.setCreated(LocalDateTime.of(2000, 5, 5, 5, 5, 5));
        RequestDto createdRequest = requestService.create(requestDto);
        RequestDto returnedRequest = requestService.getById(user.getId(), createdRequest.getId());

        assertEquals(createdRequest.getDescription(), returnedRequest.getDescription());
        assertEquals(createdRequest.getCreated(), returnedRequest.getCreated());
    }

    @Test
    void getByIncorrectIdTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.getById(9999L, 9999L));
    }

    @Test
    void getByIncorrectUserIdTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.getById(9999L, 9999L));
    }

    @Test
    void getRequestByOwnerTest() {
        UserDto createdUser = userService.create(userDto2);
        requestDto.setRequesterId(createdUser.getId());
        requestDto.setCreated(LocalDateTime.of(2000, 5, 5, 5, 5, 5));
        RequestDto createdRequest = requestService.create(requestDto);
        List<RequestDto> returnedRequests = requestService.getRequestByOwner(createdUser.getId());

        assertFalse(returnedRequests.isEmpty());
        assertTrue(returnedRequests.contains(createdRequest));
    }

    @Test
    void getByIncorrectOwnerIdTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.getRequestByOwner(9999L));
    }

    @Test
    void getAllByIncorrectUserIdTest() {
        Pageable pageable = PageRequest.of(1, 20, Sort.by(DESC, "created"));

        assertThrows(NotFoundException.class,
                () -> requestService.getAllRequests(9999L, pageable));
    }

    @Test
    void getAllWithNullSizeTest() {
        UserDto user1 = userService.create(userDto1);
        UserDto user2 = userService.create(userDto2);
        requestDto.setRequesterId(user1.getId());
        requestDto.setCreated(LocalDateTime.of(2000, 5, 5, 5, 5, 5));
        RequestDto createdRequest = requestService.create(requestDto);
        Pageable pageable = PageRequest.of(0, 20, Sort.by(DESC, "created"));

        List<RequestDto> returnedRequests = requestService.getAllRequests(user2.getId(), pageable);

        assertFalse(returnedRequests.isEmpty());
        assertTrue(returnedRequests.contains(createdRequest));
    }


}