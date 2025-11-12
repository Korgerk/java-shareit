package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

import static ru.practicum.shareit.booking.BookingController.BOOKINGS_PATH;

@RestController
@RequestMapping(path = BOOKINGS_PATH)
@RequiredArgsConstructor
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

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody BookingDto bookingDto) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping(BOOKING_ID_PATH)
    public BookingDto updateStatus(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingService.updateStatus(bookingId, userId, approved);
    }

    @GetMapping(BOOKING_ID_PATH)
    public BookingDto getById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long bookingId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestHeader(USER_ID_HEADER) Long userId, @RequestParam(defaultValue = DEFAULT_STATE) String state, @RequestParam(defaultValue = DEFAULT_FROM) Integer from, @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        return bookingService.getAllByBooker(userId, state, from, size);
    }

    @GetMapping(OWNER_PATH)
    public List<BookingDto> getAllByOwner(@RequestHeader(USER_ID_HEADER) Long userId, @RequestParam(defaultValue = DEFAULT_STATE) String state, @RequestParam(defaultValue = DEFAULT_FROM) Integer from, @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        return bookingService.getAllByOwner(userId, state, from, size);
    }
}