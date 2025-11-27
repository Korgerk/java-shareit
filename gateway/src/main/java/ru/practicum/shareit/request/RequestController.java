package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @RequestBody @Valid
            CreateItemRequestDto request
    ) {
        log.info("Request from userId={} for add request with data: {}", userId, request);
        return requestClient.addRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItemRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("Request from userId={} for get his requests", userId);
        return requestClient.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersItemRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("Request from userId={} for get other users requests", userId);
        return requestClient.getOtherUsersItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable long requestId) {
        log.info("Request for get request with id={}", requestId);
        return requestClient.getItemRequest(requestId);
    }
}