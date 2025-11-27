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
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @RequestBody @Valid
            CreateItemDto item
    ) {
        log.info("Request from userId={} for add item with data: {}", userId, item.toString());
        return itemClient.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> addItem(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "item id should be positive number")
            long itemId,
            @RequestBody @Valid
            UpdateItemDto item
    ) {
        log.info("Request from userId={} for update item with id={} data: {}", userId, itemId, item.toString());
        return itemClient.updateItem(userId, itemId, item);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("Request from userId={} for get his items", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "item id should be positive number")
            long itemId
    ) {
        log.info("Request from userId={} for get item with id={}", userId, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @RequestParam @NotBlank
            String text
    ) {
        log.info("Request from userId={} for search items by text={}", userId, text);
        return itemClient.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "item id should be positive number")
            long itemId,
            @RequestBody @Valid
            CreateCommentDto comment
    ) {
        log.info("Request from userId={} for item with id={} to add comment: {}", userId, itemId, comment.toString());
        return itemClient.addComment(userId, itemId, comment);
    }
}