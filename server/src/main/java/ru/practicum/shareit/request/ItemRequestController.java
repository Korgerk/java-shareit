package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody ItemRequestDto requestDto) {
        return itemRequestService.create(requestDto, userId);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> getAllByRequestor(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.getAllByRequestor(userId);
    }

    @GetMapping(ALL_PATH)
    public List<ItemRequestWithItemsDto> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                        @RequestParam(defaultValue = DEFAULT_FROM) Integer from,
                                                        @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping(REQUEST_ID_PATH)
    public ItemRequestWithItemsDto getById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long requestId) {
        return itemRequestService.getById(requestId, userId);
    }
}