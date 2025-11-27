package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    long id;
    BookingItemDto item;
    BookingUserDto booker;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;

}