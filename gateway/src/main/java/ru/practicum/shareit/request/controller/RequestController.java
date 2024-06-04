package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @Autowired
    public RequestController(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) long userId,
                                         @Valid @RequestBody RequestCreateDto request) {
        log.info("Creating request by user " + userId);
        ResponseEntity<Object> createdRequest = requestClient.create(request, userId);
        log.info("Created request by user " + userId);
        return createdRequest;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_ID_HEADER) long userId,
                                          @PathVariable long requestId) {
        log.info("Getting request by user " + userId);
        ResponseEntity<Object> gotResult = requestClient.getById(userId, requestId);
        log.info("Got request by user " + userId);
        return gotResult;
    }


    @GetMapping
    public ResponseEntity<Object> getByOwner(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Getting request by owner " + userId);
        ResponseEntity<Object> gotResult = requestClient.getByOwner(userId);
        log.info("Got request by owner " + userId);
        return gotResult;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID_HEADER) long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("Getting all by user " + userId);
        ResponseEntity<Object> result = requestClient.getAll(userId, from, size);
        log.info("Got all by user " + userId);
        return result;
    }
}