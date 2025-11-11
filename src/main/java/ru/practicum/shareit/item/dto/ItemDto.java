package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Item name cannot be blank")
    private String name;

    @NotBlank(message = "Item description cannot be blank")
    private String description;

    @NotNull(message = "Item availability cannot be null")
    private Boolean available;

    private Long ownerId;
    private Long requestId;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;

    @JsonIgnore
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @JsonIgnore
    public void setLastBooking(BookingShortDto lastBooking) {
        this.lastBooking = lastBooking;
    }

    @JsonIgnore
    public void setNextBooking(BookingShortDto nextBooking) {
        this.nextBooking = nextBooking;
    }

    @JsonIgnore
    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
}