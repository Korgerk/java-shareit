package ru.practicum.shareit.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BookingDto {
    private Long id;

    @Future(message = "Дата начала бронирования должна быть в будущем")
    @NotNull(message = "Дата начала бронирования обязательна")
    private LocalDateTime start;

    @Future(message = "Дата окончания бронирования должна быть в будущем")
    @NotNull(message = "Дата окончания бронирования обязательна")
    private LocalDateTime end;

    @NotNull(message = "ID вещи обязателен")
    private Long itemId;

    private BookingItemDto item;
    private BookingUserDto booker;
    private BookingStatus status;
}