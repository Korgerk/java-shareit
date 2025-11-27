package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = ItemController.BASE_PATH)
public class ItemController {

    public static final String BASE_PATH = "/items";
    public static final String ITEM_PATH = "/{itemId}";
    public static final String SEARCH_PATH = "/search";
    public static final String COMMENT_PATH = "/{itemId}/comment";

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String POSITIVE_USER_ID_MESSAGE = "user id should be positive number";
    public static final String POSITIVE_ITEM_ID_MESSAGE = "item id should be positive number";

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @RequestBody @Valid
            CreateItemDto item
    ) {
        log.info("Request from userId={} for add item with data: {}", userId, item.toString());
        return itemClient.addItem(userId, item);
    }

    @PatchMapping(ITEM_PATH)
    public ResponseEntity<Object> updateItem(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_ITEM_ID_MESSAGE)
            long itemId,
            @RequestBody @Valid
            UpdateItemDto item
    ) {
        log.info("Request from userId={} for update item with id={} data: {}", userId, itemId, item.toString());
        return itemClient.updateItem(userId, itemId, item);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("Request from userId={} for get his items", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping(ITEM_PATH)
    public ResponseEntity<Object> getItem(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_ITEM_ID_MESSAGE)
            long itemId
    ) {
        log.info("Request from userId={} for get item with id={}", userId, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping(SEARCH_PATH)
    public ResponseEntity<Object> searchItems(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @RequestParam @NotBlank
            String text
    ) {
        log.info("Request from userId={} for search items by text={}", userId, text);
        return itemClient.searchItems(userId, text);
    }

    @PostMapping(COMMENT_PATH)
    public ResponseEntity<Object> addComment(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_ITEM_ID_MESSAGE)
            long itemId,
            @RequestBody @Valid
            CreateCommentDto comment
    ) {
        log.info("Request from userId={} for item with id={} to add comment: {}", userId, itemId, comment.toString());
        return itemClient.addComment(userId, itemId, comment);
    }
}