package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = ItemRequestController.BASE_PATH)
public class ItemRequestController {

    public static final String BASE_PATH = "/requests";
    public static final String ALL_PATH = "/all";
    public static final String GET_BY_ID_PATH = "/{requestId}";

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String POSITIVE_USER_ID_MESSAGE = "user id should be positive number";

    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @RequestBody @Valid
            CreateItemRequestDto request
    ) {
        log.info("TEST Request from user_id={} to create item request={}", userId, request);
        return itemRequestService.createRequest(userId, request);
    }

    @GetMapping
    public List<ItemRequestDto> getUserItemRequests(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("TEST Request from user_id={} to get own requests", userId);
        return itemRequestService.getUserRequests(userId);
    }

    @GetMapping(ALL_PATH)
    public List<ItemRequestDto> getOtherUsersItemRequests(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("TEST Request from user_id={} to get another users requests", userId);
        return itemRequestService.getOtherUsersRequests(userId);
    }

    @GetMapping(GET_BY_ID_PATH)
    public ItemRequestDto getItemRequest(@PathVariable long requestId) {
        log.info("TEST Request for getting item request with id={}", requestId);
        return itemRequestService.getItemRequest(requestId);
    }
}