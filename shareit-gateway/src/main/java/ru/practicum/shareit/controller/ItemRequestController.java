package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequestor(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getAllByRequestor(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "0") @PositiveOrZero Integer from, @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        return itemRequestClient.getById(userId, requestId);
    }
}