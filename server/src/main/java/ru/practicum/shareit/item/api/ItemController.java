package ru.practicum.shareit.item.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(
            @RequestHeader("X-Sharer-User-Id") @Positive
            long userId,
            @RequestBody @Valid
            CreateItemDto item
    ) {
        log.info("TEST Request from user_id={} to add item: {}", userId, item.toString());
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "item id should be positive number")
            long itemId,
            @RequestBody @Valid
            UpdateItemDto item
    ) {
        log.info("TEST Request from user_id={} to update item with id={}", userId, itemId);
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping
    public List<ItemDto> getUserItems(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        log.info("TEST Request from user_id={} to get items", userId);
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemWithAdditionalInfoDto getItem(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "item id should be positive number")
            long itemId
    ) {
        log.info("TEST Request from user_id={} to get item={}", userId, itemId);
        return itemService.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @RequestParam
            String text
    ) {
        log.info("TEST Request from user_id={} to search item by text={}", userId, text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "item id should be positive number")
            long itemId,
            @RequestBody @Valid
            CreateCommentDto comment
    ) {
        log.info("Request from user={} to create comment for item={}", userId, itemId);
        return itemService.addComment(userId, itemId, comment);
    }

}