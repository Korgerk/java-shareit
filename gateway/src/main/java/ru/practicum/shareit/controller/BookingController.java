package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BookingClient;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.client.ItemRequestClient;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.dto.BookingDto;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;
import ru.practicum.shareit.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.dto.UserDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    static final String USER_ID_HEADER = "X-Sharer-User-Id";
    static final String BOOKINGS_PATH = "/bookings";
    static final String BOOKING_ID_PATH = "/{bookingId}";
    static final String OWNER_PATH = "/owner";
    static final String STATE_PARAM = "state";
    static final String FROM_PARAM = "from";
    static final String SIZE_PARAM = "size";
    static final String DEFAULT_FROM = "0";
    static final String DEFAULT_SIZE = "10";
    static final String DEFAULT_STATE = "ALL";

    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody @Valid BookingDto bookingDto) {
        return (BookingDto) bookingClient.create(userId, bookingDto).getBody();
    }

    @PatchMapping(BOOKING_ID_PATH)
    public BookingDto updateStatus(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return (BookingDto) bookingClient.updateStatus(userId, bookingId, approved).getBody();
    }

    @GetMapping(BOOKING_ID_PATH)
    public BookingDto getById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long bookingId) {
        return (BookingDto) bookingClient.getById(userId, bookingId).getBody();
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestHeader(USER_ID_HEADER) Long userId,
                                           @RequestParam(defaultValue = DEFAULT_STATE) String state,
                                           @RequestParam(defaultValue = DEFAULT_FROM) @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = DEFAULT_SIZE) @Positive Integer size) {
        return (List<BookingDto>) bookingClient.getAllByBooker(userId, state, from, size).getBody();
    }

    @GetMapping(OWNER_PATH)
    public List<BookingDto> getAllByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @RequestParam(defaultValue = DEFAULT_STATE) String state,
                                          @RequestParam(defaultValue = DEFAULT_FROM) @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = DEFAULT_SIZE) @Positive Integer size) {
        return (List<BookingDto>) bookingClient.getAllByOwner(userId, state, from, size).getBody();
    }
}