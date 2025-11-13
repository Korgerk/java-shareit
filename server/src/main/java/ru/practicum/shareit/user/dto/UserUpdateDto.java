package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.Email;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDto {
    String name;

    @Email
    String email;
}