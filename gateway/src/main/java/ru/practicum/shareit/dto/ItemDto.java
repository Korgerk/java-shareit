package ru.practicum.shareit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Поле 'available' обязательно")
    private Boolean available;

    private Long requestId;

    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;

    @Setter
    @Getter
    public static class BookingShortDto {
        private Long id;
        private Long bookerId;
    }

    @Setter
    @Getter
    public static class CommentDto {
        private Long id;

        @NotBlank(message = "Текст комментария не может быть пустым")
        private String text;

        private String authorName;
        private java.time.LocalDateTime created;
    }
}