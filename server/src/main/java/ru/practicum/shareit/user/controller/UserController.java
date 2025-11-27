package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = UserController.BASE_PATH)
public class UserController {

    public static final String BASE_PATH = "/users";
    public static final String ID_PATH = "/{id}";

    public static final String POSITIVE_ID_MESSAGE = "id should be positive number";

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(
            @RequestBody @Valid
            CreateUserDto user
    ) {
        log.info("TEST Request to create user with data: {}", user.toString());
        return userService.createUser(user);
    }

    @GetMapping(ID_PATH)
    public UserDto getUser(
            @PathVariable @Positive(message = POSITIVE_ID_MESSAGE)
            long id
    ) {
        log.info("TEST Request to get user={}", id);
        return userService.getUser(id);
    }

    @PatchMapping(ID_PATH)
    public UserDto updateUser(
            @PathVariable @Positive(message = POSITIVE_ID_MESSAGE)
            long id,
            @RequestBody @Valid
            UpdateUserDto updateUserDto
    ) {
        log.info("TEST Request to update user with id: {}", id);
        return userService.updateUser(id, updateUserDto);
    }

    @DeleteMapping(ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable @Positive(message = POSITIVE_ID_MESSAGE)
            long id
    ) {
        log.info("TEST Request to delete user with id: {}", id);
        userService.deleteUser(id);
    }
}