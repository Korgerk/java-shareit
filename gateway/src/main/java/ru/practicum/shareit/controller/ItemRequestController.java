package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemRequestClient;
import ru.practicum.shareit.dto.ItemRequestDto;
import ru.practicum.shareit.dto.ItemRequestWithItemsDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    static final String USER_ID_HEADER = "X-Sharer-User-Id";
    static final String REQUEST_ID_PATH = "/{requestId}";
    static final String ALL_PATH = "/all";
    static final String FROM_PARAM = "from";
    static final String SIZE_PARAM = "size";
    static final String DEFAULT_FROM = "0";
    static final String DEFAULT_SIZE = "10";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody @Valid ItemRequestDto requestDto) {
        return (ItemRequestDto) itemRequestClient.create(userId, requestDto).getBody();
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> getAllByRequestor(@RequestHeader(USER_ID_HEADER) Long userId) {
        return (List<ItemRequestWithItemsDto>) itemRequestClient.getAllByRequestor(userId).getBody();
    }

    @GetMapping(ALL_PATH)
    public List<ItemRequestWithItemsDto> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                        @RequestParam(defaultValue = DEFAULT_FROM) @PositiveOrZero Integer from,
                                                        @RequestParam(defaultValue = DEFAULT_SIZE) @Positive Integer size) {
        return (List<ItemRequestWithItemsDto>) itemRequestClient.getAllRequests(userId, from, size).getBody();
    }

    @GetMapping(REQUEST_ID_PATH)
    public ItemRequestWithItemsDto getById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long requestId) {
        return (ItemRequestWithItemsDto) itemRequestClient.getById(userId, requestId).getBody();
    }
}