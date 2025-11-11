package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable @Positive Long id, @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        ItemDto itemDto = itemService.getItemById(userId, id);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @RequestParam(defaultValue = "0") @PositiveOrZero Integer from, @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemService.getAllItems(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @Valid @RequestBody ItemDto itemDto) {
        ItemDto createdItemDto = itemService.createItem(userId, itemDto);
        return ResponseEntity.status(201).body(createdItemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @PathVariable @Positive Long id, @Valid @RequestBody ItemDto itemDto) {
        ItemDto updatedItemDto = itemService.updateItem(userId, id, itemDto);
        return ResponseEntity.ok(updatedItemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text, @RequestParam(defaultValue = "0") @PositiveOrZero Integer from, @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@RequestHeader("X-Sharer-User-Id") @Positive Long userId, @PathVariable @Positive Long itemId, @RequestBody @Valid String text) {
        CommentDto commentDto = itemService.addComment(itemId, userId, text);
        return ResponseEntity.status(201).body(commentDto);
    }
}