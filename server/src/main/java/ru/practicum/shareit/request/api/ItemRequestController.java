package ru.practicum.shareit.request.api;

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
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @RequestBody @Valid
            CreateItemRequestDto request
    ) {
        log.info("TEST Request from user_id={} to create item request={}", userId, request);
        return itemRequestService.createRequest(userId, request);
    }

    @GetMapping
    public List<ItemRequestDto> getUserItemRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("TEST Request from user_id={} to get own requests", userId);
        return itemRequestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherUsersItemRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("TEST Request from user_id={} to get another users requests", userId);
        return itemRequestService.getOtherUsersRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@PathVariable long requestId) {
        log.info("TEST Request for getting item request with id={}", requestId);
        return itemRequestService.getItemRequest(requestId);
    }

}