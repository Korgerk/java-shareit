package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingShortDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long bookerId;
    String status;
}