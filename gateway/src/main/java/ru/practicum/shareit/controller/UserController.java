package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.dto.UserDto;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        return (UserDto) userClient.create(userDto).getBody();
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto userDto) {
        return (UserDto) userClient.update(id, userDto).getBody();
    }

    @GetMapping
    public List<UserDto> getAll() {
        return (List<UserDto>) userClient.getAll().getBody();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return (UserDto) userClient.getById(id).getBody();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userClient.delete(id);
    }
}