package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {

    private static final String REQUEST_HEADER = "X-Sharer-User-Id";


    private final RequestService requestService;

    @PostMapping
    public RequestDto create(@RequestHeader(REQUEST_HEADER) Long userId,
                             @Valid @RequestBody RequestCreateDto request) {
        request.setRequesterId(userId);
        request.setCreated(LocalDateTime.now());

        log.info("Creating request by user " + userId);

        RequestDto newRequest = requestService.create(request);

        log.info("Created request by user " + userId);

        return newRequest;
    }

    @GetMapping("/{requestId}")
    public RequestDto getById(@RequestHeader(REQUEST_HEADER) Long userId,
                              @PathVariable("requestId") Long requestId) {
        log.info("Getting request by user " + userId);
        RequestDto gotResult = requestService.getById(userId, requestId);
        log.info("Got request by user " + userId);
        return gotResult;
    }

    @GetMapping
    public List<RequestDto> getByOwner(@RequestHeader(REQUEST_HEADER) Long ownerId) {
        log.info("Getting request by owner " + ownerId);
        List<RequestDto> gotResult = requestService.getRequestByOwner(ownerId);
        log.info("Got request by owner " + ownerId);
        return gotResult;
    }

    @GetMapping("/all")
    public List<RequestDto> getAll(@RequestHeader(REQUEST_HEADER) long userId,
                                   @RequestParam(defaultValue = "0") Integer from,
                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Getting all by user " + userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from, size, sort);
        List<RequestDto> result = requestService.getAllRequests(userId, pageable);
        log.info("Got all by user " + userId);
        return result;
    }
}
