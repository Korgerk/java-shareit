package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.booking.dto.BookingDto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.client.BookingClient;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingDto bookingDto) {
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId, @RequestParam(name = "approved") Boolean approved) {
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state, @PositiveOrZero @RequestParam(defaultValue = "0") Integer from, @Positive @RequestParam(defaultValue = "10") Integer size) {
        BookingStatus bookingState = BookingStatus.from(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.getBookingsByBookerId(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state, @PositiveOrZero @RequestParam(defaultValue = "0") Integer from, @Positive @RequestParam(defaultValue = "10") Integer size) {
        BookingStatus bookingState = BookingStatus.from(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.getBookingsByOwnerId(userId, bookingState, from, size);
    }
}