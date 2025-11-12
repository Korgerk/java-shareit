package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingShortDto lastBooking;
    BookingShortDto nextBooking;
    List<CommentDto> comments;
    Long requestId;
}