package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBookingDto {
    @NotNull
    @Positive
    long itemId;

    @NotNull
    @FutureOrPresent
    LocalDateTime start;

    @NotNull
    @Future
    LocalDateTime end;
}