package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemWithAdditionalInfoDto {
    long id;
    String name;
    String description;
    boolean available;
    ItemBookingDateDto lastBooking;
    ItemBookingDateDto nextBooking;
    List<CommentDto> comments;
}