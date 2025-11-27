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
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @RequestBody @Valid
            CreateBookingDto booking
    ) {
        log.info("TEST Request from user={} for create booking={}", userId, booking);
        return bookingService.addBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "booking id should be positive number")
            long bookingId,
            @RequestParam @NotNull
            Boolean approved
    ) {
        log.info("TEST Request from user={} for approve={} booking={}", userId, approved, bookingId);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "booking id should be positive number")
            long bookingId
    ) {
        log.info("TEST Request from user={} for get booking={}", userId, bookingId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("TEST Request from user={} for get bookings", userId);
        return bookingService.getUserBookings(userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("TEST Request from user={} for get owner bookings", userId);
        return bookingService.getOwnerBookings(userId);
    }

}