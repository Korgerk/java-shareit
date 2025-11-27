package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

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

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @RequestBody @Valid
            CreateBookingDto booking
    ) {
        log.info("Request from userId={} for create booking: {}", userId, booking);
        return bookingClient.createBooking(userId, booking);
    }

    @PatchMapping(BOOKING_ID_PATH)
    public ResponseEntity<Object> approveBooking(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_BOOKING_ID_MESSAGE)
            long bookingId,
            @RequestParam @NotNull
            Boolean approved
    ) {
        log.info("Request from userId={} to set approve={} for bookingId={}", userId, approved, bookingId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping(BOOKING_ID_PATH)
    public ResponseEntity<Object> getBooking(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_BOOKING_ID_MESSAGE)
            long bookingId
    ) {
        log.info("Request from userId={} for get bookingId={}", userId, bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("Request from userId={} for get his bookings", userId);
        return bookingClient.getUserBookings(userId);
    }

    @GetMapping(OWNER_PATH)
    public ResponseEntity<Object> getOwnerBookings(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("Request from userId={} for get owner bookings", userId);
        return bookingClient.getOwnerBookings(userId);
    }
}