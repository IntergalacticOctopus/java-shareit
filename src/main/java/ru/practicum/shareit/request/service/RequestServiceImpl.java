package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final RequestMapper requestMapper;

    @Override
    public RequestDto create(RequestCreateDto request) {
        userRepository.findById(request.getRequesterId()).orElseThrow(() -> new NotFoundException("User does not exist"));
        RequestDto createdRequest = requestMapper.toRequestDto(requestRepository.save(requestMapper.toRequest(request)));
        return createdRequest;
    }

    @Override
    public RequestDto getById(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exist"));
        RequestDto request = requestMapper.toRequestDto(requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request does not exist")));
        return request;
    }

    @Override
    public List<RequestDto> getRequestByOwner(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exist"));
        List<RequestDto> requestDtos = new ArrayList<>();
        for (Request request : requestRepository.findAllByRequesterId(userId, Sort.by(DESC, "created"))) {
            requestDtos.add(requestMapper.toRequestDto(request));
        }
        return requestDtos;
    }

    @Override
    public List<RequestDto> getAllRequests(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exist"));
        List<Request> page = requestRepository.findAllByRequesterIdNot(userId, pageable);
        List<RequestDto> requestDtos = new ArrayList<>();
        for (Request request : page) {
            requestDtos.add(requestMapper.toRequestDto(request));
        }
        return requestDtos;
    }
}
