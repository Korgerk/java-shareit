package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.util.List;


@Data
public class ItemWithAdditionalInfoDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private ItemBookingDateDto lastBooking;
    private ItemBookingDateDto nextBooking;
    private List<CommentDto> comments;
}