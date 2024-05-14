package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserDto create(UserCreateDto user) {
        User returnUser = userRepository.save(userMapper.toUser(user));
        return userMapper.toUserDto(returnUser);
    }

    @Override
    @Transactional
    public UserDto update(UserUpdateDto userUpdateDto) {
        return userRepository.findById(userUpdateDto.getId())
                .map(user -> {
                    updateUserData(user, userUpdateDto);
                    userRepository.save(user);
                    return userMapper.toUserDto(user);
                })
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void updateUserData(User user, UserUpdateDto userUpdateDto) {
        user.setEmail(userUpdateDto.getEmail() != null ? userUpdateDto.getEmail() : user.getEmail());
        user.setName(userUpdateDto.getName() != null ? userUpdateDto.getName() : user.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        UserDto user = userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist")));
        return user;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        List<UserDto> userDtoList = userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        return userDtoList;
    }
}
