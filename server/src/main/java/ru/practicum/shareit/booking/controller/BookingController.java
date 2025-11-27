package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = BookingController.BASE_PATH)
public class BookingController {

    public static final String BASE_PATH = "/bookings";
    public static final String BOOKING_ID_PATH = "/{bookingId}";
    public static final String OWNER_PATH = "/owner";

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String POSITIVE_USER_ID_MESSAGE = "user id should be positive number";
    public static final String POSITIVE_BOOKING_ID_MESSAGE = "booking id should be positive number";

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @RequestBody @Valid
            CreateBookingDto booking
    ) {
        log.info("TEST Request from user={} for create booking={}", userId, booking);
        return bookingService.addBooking(userId, booking);
    }

    @PatchMapping(BOOKING_ID_PATH)
    public BookingDto approveBooking(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_BOOKING_ID_MESSAGE)
            long bookingId,
            @RequestParam @NotNull
            Boolean approved
    ) {
        log.info("TEST Request from user={} for approve={} booking={}", userId, approved, bookingId);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping(BOOKING_ID_PATH)
    public BookingDto getBooking(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_BOOKING_ID_MESSAGE)
            long bookingId
    ) {
        log.info("TEST Request from user={} for get booking={}", userId, bookingId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("TEST Request from user={} for get bookings", userId);
        return bookingService.getUserBookings(userId);
    }

    @GetMapping(OWNER_PATH)
    public List<BookingDto> getOwnerBookings(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("TEST Request from user={} for get owner bookings", userId);
        return bookingService.getOwnerBookings(userId);
    }
}