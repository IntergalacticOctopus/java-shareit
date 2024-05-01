package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserDto create(UserCreateDto user) {
        User returnUser = userRepository.save(userMapper.toUser(user));
        return userMapper.toUserDto(returnUser);
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findById(userUpdateDto.getId());
        userOptional.ifPresentOrElse(
                user -> {
                    updateUserData(user, userUpdateDto);
                    userRepository.save(user);
                },
                () -> {
                    throw new NotFoundException("User not found");
                }
        );
        return userMapper.toUserDto(userOptional.orElseThrow(() -> new NotFoundException("User not found")));
    }

    private void updateUserData(User user, UserUpdateDto userUpdateDto) {
        user.setEmail(userUpdateDto.getEmail() != null ? userUpdateDto.getEmail() : user.getEmail());
        user.setName(userUpdateDto.getName() != null ? userUpdateDto.getName() : user.getName());
    }

    @Override
    public UserDto getUserById(Long id) {
        UserDto user = userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
        return user;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> list = new ArrayList<>(userRepository.findAll());
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : list) {
            userDtoList.add(userMapper.toUserDto(user));
        }
        return userDtoList;
    }
}
