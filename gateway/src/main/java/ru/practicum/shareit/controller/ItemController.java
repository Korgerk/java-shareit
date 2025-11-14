package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.dto.ItemDto;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) throws BadRequestException {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @Valid @RequestBody ItemDto itemDto) throws BadRequestException {
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, MultiValueMap<String, String> params) throws BadRequestException {
        return itemClient.getById(userId, itemId, params);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId, MultiValueMap<String, String> params) throws BadRequestException {
        return itemClient.getOwnerItems(userId, params);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam MultiValueMap<String, String> params) throws BadRequestException {
        String text = params.getFirst("text");
        if (text != null && text.isBlank()) {
            params.remove("text");
        }
        return itemClient.search(params);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @Valid @RequestBody ItemClient.CommentDto commentDto) throws BadRequestException {
        return itemClient.addComment(userId, itemId, commentDto.getText());
    }
}