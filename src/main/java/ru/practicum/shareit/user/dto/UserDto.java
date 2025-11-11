package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {
    private Long id;

    @NotBlank(message = "User name cannot be blank")
    private String name;

    @NotBlank(message = "User email cannot be blank")
    @Email(message = "User email must be a well-formed email address")
    private String email;

    public UserDto() {
    }

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}