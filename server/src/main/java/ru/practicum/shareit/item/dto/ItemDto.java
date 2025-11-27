package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    long id;
    String name;
    String description;
    boolean available;
}