package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingUserDto {
    long id;
    String name;
}