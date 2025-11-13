package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, CreateBookingDto createBooking);

    BookingDto approveBooking(long userId, long bookingIid, boolean isApproved);

    BookingDto getBooking(long userId, long bookingId);

    List<BookingDto> getUserBookings(long userId);

    List<BookingDto> getOwnerBookings(long userId);
}