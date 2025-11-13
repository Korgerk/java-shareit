package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(
            @RequestBody @Valid
            CreateUserDto user
    ) {
        log.info("Request to create user with data: {}", user.toString());
        return userClient.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(
            @PathVariable @Positive(message = "id should be positive number")
            long id
    ) {
        log.info("Request to get user with id={}", id);
        return userClient.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @PathVariable @Positive(message = "id should be positive number")
            long id,
            @RequestBody @Valid
            UpdateUserDto user
    ) {
        log.info("Request to update user with id= {} by data: {}", id, user.toString());
        return userClient.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable @Positive(message = "id should be positive number")
            long id
    ) {
        log.info("Request to delete user with id={}", id);
        return userClient.deleteUser(id);
    }
}