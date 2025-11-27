package ru.practicum.shareit.item.controller;

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
@RequestMapping(path = ItemController.BASE_PATH)
public class ItemController {

    public static final String BASE_PATH = "/items";
    public static final String ITEM_ID_PATH = "/{itemId}";
    public static final String SEARCH_PATH = "/search";
    public static final String COMMENT_PATH = "/{itemId}/comment";

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String POSITIVE_USER_ID_MESSAGE = "user id should be positive number";
    public static final String POSITIVE_ITEM_ID_MESSAGE = "item id should be positive number";

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @RequestBody @Valid
            CreateItemDto item
    ) {
        log.info("TEST Request from user_id={} to add item: {}", userId, item.toString());
        return itemService.addItem(userId, item);
    }

    @PatchMapping(ITEM_ID_PATH)
    public ItemDto updateItem(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_ITEM_ID_MESSAGE)
            long itemId,
            @RequestBody @Valid
            UpdateItemDto item
    ) {
        log.info("TEST Request from user_id={} to update item with id={}", userId, itemId);
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping
    public List<ItemDto> getUserItems(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId
    ) {
        log.info("TEST Request from user_id={} to get items", userId);
        return itemService.getItems(userId);
    }

    @GetMapping(ITEM_ID_PATH)
    public ItemWithAdditionalInfoDto getItem(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_ITEM_ID_MESSAGE)
            long itemId
    ) {
        log.info("TEST Request from user_id={} to get item={}", userId, itemId);
        return itemService.getItem(itemId, userId);
    }

    @GetMapping(SEARCH_PATH)
    public List<ItemDto> searchItems(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @RequestParam String text
    ) {
        log.info("TEST Request from user_id={} to search item by text={}", userId, text);
        return itemService.searchItems(text);
    }

    @PostMapping(COMMENT_PATH)
    public CommentDto addComment(
            @RequestHeader(USER_ID_HEADER) @Positive(message = POSITIVE_USER_ID_MESSAGE)
            long userId,
            @PathVariable @Positive(message = POSITIVE_ITEM_ID_MESSAGE)
            long itemId,
            @RequestBody @Valid
            CreateCommentDto comment
    ) {
        log.info("Request from user={} to create comment for item={}", userId, itemId);
        return itemService.addComment(userId, itemId, comment);
    }
}