package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Getter
@Setter
public class ItemDto {
    private Long id;

    @NotBlank(message = "Item name cannot be empty")
    private String name;

    @NotBlank(message = "Item description cannot be empty")
    private String description;

    @NotNull(message = "Item availability cannot be null")
    private Boolean available;

    private Long ownerId;
    private Long requestId;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;

    public ItemDto() {
    }

    public ItemDto(String name, String description, Boolean available, Long ownerId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
    }
}