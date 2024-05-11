package ru.practicum.shareit.userTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(classes = ShareItApp.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final UserService userService;

    private final UserCreateDto userCreate = new UserCreateDto(1L, "user1", "user1@gmail.com");

    @Test
    void getAllUsersTest() {
        UserDto first = userService.create(new UserCreateDto(3L, "user123", "usr@yandex.ru"));
        UserDto second = userService.create(new UserCreateDto(5L, "user12345", "usr123@yandex.ru"));
        assertEquals(2, userService.getUsers().size());
        System.out.println(userService.getUsers());
        assertTrue(userService.getUsers().contains(first));
        assertTrue(userService.getUsers().contains(second));
    }

    @Test
    void updateUserTest() {
        UserDto user = userService.create(userCreate);
        UserUpdateDto userUpdateDto = new UserUpdateDto(user.getId(), "user1Dto", null);
        userUpdateDto.setId(user.getId());
        UserDto updatedUser = userService.update(userUpdateDto);
        assertEquals(updatedUser.getEmail(), user.getEmail());
    }

    @Test
    void updateWithNullNameTest() {
        UserDto user = userService.create(userCreate);
        user.setName(null);
        user.setEmail("User@yandex.ru");
        UserUpdateDto userUpdateDto = new UserUpdateDto(user.getId(), null, "User@yandex.ru");
        userUpdateDto.setId(user.getId());
        UserDto updatedUser = userService.update(userUpdateDto);
        assertEquals(updatedUser.getEmail(), user.getEmail());
    }

    @Test
    void updateWithNullEmailTest() {
        UserDto user = userService.create(userCreate);
        user.setName("newUser");
        user.setEmail(null);
        UserUpdateDto userUpdateDto = new UserUpdateDto(user.getId(), "newUser", null);
        UserDto updatedUser = userService.update(userUpdateDto);
        assertEquals(updatedUser.getName(), user.getName());
    }

    @Test
    void deleteByIdTest() {
        UserDto user = userService.create(userCreate);
        userService.deleteById(user.getId());
        assertTrue(userService.getUsers().isEmpty());
    }

}