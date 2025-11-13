package ru.practicum.shareit.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDto {
    private Long id;
    @NotNull(message = "Дата начала бронирования не может быть пустой")
    @Future(message = "Дата начала бронирования должна быть в будущем")
    private LocalDateTime start;
    @NotNull(message = "Дата окончания бронирования не может быть пустой")
    @Future(message = "Дата окончания бронирования должна быть в будущем")
    private LocalDateTime end;
    @NotNull(message = "ID вещи не может быть пустым")
    private Long itemId;
    private BookingItemDto item;
    private BookingUserDto booker;
    private BookingStatus status;
}