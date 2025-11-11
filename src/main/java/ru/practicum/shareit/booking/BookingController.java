package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable @Positive Long bookingId, @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        BookingDto bookingDto = bookingService.getBookingById(userId, bookingId);
        return ResponseEntity.ok(bookingDto);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @RequestParam(defaultValue = "ALL") String state, @RequestParam(defaultValue = "0") @PositiveOrZero Integer from, @RequestParam(defaultValue = "10") @Positive Integer size) {
        BookingState bookingState = BookingState.fromString(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingService.getAllBookingsByUser(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByOwnerItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @RequestParam(defaultValue = "ALL") String state, @RequestParam(defaultValue = "0") @PositiveOrZero Integer from, @RequestParam(defaultValue = "10") @Positive Integer size) {
        BookingState bookingState = BookingState.fromString(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingService.getAllBookingsByOwnerItems(userId, bookingState, from, size);
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @Valid @RequestBody BookingDto bookingDto) {
        BookingDto createdBookingDto = bookingService.createBooking(userId, bookingDto);
        return ResponseEntity.status(201).body(createdBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveOrRejectBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @PathVariable @Positive Long bookingId, @RequestParam Boolean approved) {
        BookingDto updatedBookingDto = bookingService.approveOrRejectBooking(userId, bookingId, approved);
        return ResponseEntity.ok(updatedBookingDto);
    }
}