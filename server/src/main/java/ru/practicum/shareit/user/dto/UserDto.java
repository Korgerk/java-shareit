package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;


@Data
@Builder
public class UserDto {
    private long id;
    private String name;
    private String email;
    @JsonProperty("created_at")
    private Instant created;
    @JsonProperty("updated_at")
    private Instant updated;
}