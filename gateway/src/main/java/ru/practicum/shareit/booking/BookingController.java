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
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @RequestBody @Valid
            CreateBookingDto booking
    ) {
        log.info("Request from userId={} for create booking: {}", userId, booking);
        return bookingClient.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "booking id should be positive number")
            long bookingId,
            @RequestParam @NotNull
            Boolean approved
    ) {
        log.info("Request from userId={} to set approve={} for bookingId={}", userId, approved, bookingId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "booking id should be positive number")
            long bookingId
    ) {
        log.info("Request from userId={} for get bookingId={}", userId, bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("Request from userId={} for get his bookings", userId);
        return bookingClient.getUserBookings(userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("Request from userId={} for get owner bookings", userId);
        return bookingClient.getOwnerBookings(userId);
    }
}