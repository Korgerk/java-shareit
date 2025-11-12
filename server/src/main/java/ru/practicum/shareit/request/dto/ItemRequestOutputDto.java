package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestOutputDto {
    Long id;
    String description;
    LocalDateTime created;
    List<ItemInRequestDto> items;

    @Setter
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ItemInRequestDto {
        Long id;
        String name;
        String description;
        Long ownerId;
    }
}