package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    long id;
    String name;
    String email;
    @JsonProperty("created_at")
    Instant created;
    @JsonProperty("updated_at")
    Instant updated;
}