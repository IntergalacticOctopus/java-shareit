package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.AvailableException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;
    private final UserMapperImpl mapper = new UserMapperImpl();

    private final UserCreateDto userCreated = new UserCreateDto(1L, "User", "user@gmail.com");

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, mapper);
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mapper.toUser(userCreated)));

        UserDto user = userService.getUserById(1L);
        verify(userRepository, Mockito.times(1)).findById(1L);
        assertThat(userCreated.getName(), equalTo(user.getName()));
        assertThat(userCreated.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void getByIdWithNotFoundExceptionTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class, () -> userService.getUserById(999L));
        assertEquals("User does not exist", exception.getMessage());
    }

    @Test
    void throwExceptionAvailableTest() {
        when(userRepository.save(any()))
                .thenThrow(new AvailableException("User already exist"));

        final AvailableException exception = assertThrows(
                AvailableException.class, () -> userService.create(userCreated));
        assertEquals("User already exist", exception.getMessage());
    }
}