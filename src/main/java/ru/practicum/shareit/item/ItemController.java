package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    static final String USER_ID_HEADER = "X-Sharer-User-Id";
    
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody ItemDto itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.update(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(required = false) String text) {
        return itemService.search(text);
    }
}