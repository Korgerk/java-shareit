package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    static final String USER_ID_HEADER = "X-Sharer-User-Id";
    static final String REQUEST_ID_PATH = "/{requestId}";
    static final String ALL_PATH = "/all";
    static final String FROM_PARAM = "from";
    static final String SIZE_PARAM = "size";
    static final String DEFAULT_FROM = "0";
    static final String DEFAULT_SIZE = "10";

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody ItemRequestDto requestDto) {
        return itemRequestService.create(requestDto, userId);
    }

    @GetMapping(REQUEST_ID_PATH)
    public ItemRequestDto getById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long requestId) {
        return itemRequestService.getById(requestId, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByRequestor(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.getAllByRequestor(userId);
    }

    @GetMapping(ALL_PATH)
    public List<ItemRequestDto> getAll(@RequestHeader(USER_ID_HEADER) Long userId,
                                       @RequestParam(defaultValue = DEFAULT_FROM) Integer from,
                                       @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        return itemRequestService.getAll(userId, from, size);
    }
}