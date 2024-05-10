package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestService {
    RequestDto create(RequestCreateDto request);

    RequestDto getById(Long userId, Long requestId);

    List<RequestDto> getRequestByOwner(Long userId);

    List<RequestDto> getAllRequests(Long userId, Pageable pageable);
}
