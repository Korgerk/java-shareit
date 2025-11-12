package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ItemRequestOutputDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemInRequestDto> items;

    @Setter
    @Getter
    public static class ItemInRequestDto {
        private Long id;
        private String name;
        private String description;
        private Long ownerId;
    }
}