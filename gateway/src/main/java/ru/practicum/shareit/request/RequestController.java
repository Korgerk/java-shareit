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
@RequestMapping(path = RequestController.BASE_PATH)
public class RequestController {

    public static final String BASE_PATH = "/requests";
    public static final String ALL_PATH = "/all";
    public static final String GET_BY_ID_PATH = "/{requestId}";

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String POSITIVE_USER_ID_MESSAGE = "user id should be positive number";

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @RequestBody @Valid
            CreateItemRequestDto request
    ) {
        log.info("Request from userId={} for add request with data: {}", userId, request);
        return requestClient.addRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItemRequests(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("Request from userId={} for get his requests", userId);
        return requestClient.getUserItemRequests(userId);
    }

    @GetMapping(ALL_PATH)
    public ResponseEntity<Object> getOtherUsersItemRequests(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("Request from userId={} for get other users requests", userId);
        return requestClient.getOtherUsersItemRequests(userId);
    }

    @GetMapping(GET_BY_ID_PATH)
    public ResponseEntity<Object> getItemRequest(@PathVariable long requestId) {
        log.info("Request for get request with id={}", requestId);
        return requestClient.getItemRequest(requestId);
    }
}