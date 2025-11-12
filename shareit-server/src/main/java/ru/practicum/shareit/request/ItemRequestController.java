package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestOutputDto> getAllByRequestor(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.getAllByRequestor(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutputDto> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId, @RequestParam(defaultValue = "0") Integer from, @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutputDto getById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long requestId) {
        return itemRequestService.getById(requestId, userId);
    }
}