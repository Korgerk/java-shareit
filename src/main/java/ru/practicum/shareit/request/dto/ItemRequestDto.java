package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}