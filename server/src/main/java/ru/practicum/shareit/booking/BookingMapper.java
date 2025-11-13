package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(BookingMapper.toBookingItemDto(booking.getItem()));
        bookingDto.setBooker(BookingMapper.toBookingUserDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static BookingItemDto toBookingItemDto(ru.practicum.shareit.item.model.Item item) {
        BookingItemDto itemDto = new BookingItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        return itemDto;
    }

    public static BookingUserDto toBookingUserDto(ru.practicum.shareit.user.model.User user) {
        BookingUserDto userDto = new BookingUserDto();
        userDto.setId(user.getId());
        return userDto;
    }
}