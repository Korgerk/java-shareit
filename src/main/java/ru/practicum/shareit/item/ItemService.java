package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.expectation.AccessDeniedException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final UserService userService;

    public ItemService(UserService userService) {
        this.userService = userService;
    }

    public ItemDto create(ItemDto dto, Long userId) {
        validate(dto);
        if (!userService.existsById(userId)) {
            throw new RuntimeException(String.format("Пользователь с ID %d не найден", userId));
        }
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwnerId(userId);
        item.setId(idGenerator.getAndIncrement());
        items.put(item.getId(), item);
        return toItemDto(item);
    }

    public ItemDto update(Long itemId, ItemDto dto, Long userId) {
        if (!items.containsKey(itemId)) {
            throw new RuntimeException(String.format("Пользователь с ID %d не найден", itemId));
        }
        Item item = items.get(itemId);
        if (!item.getOwnerId().equals(userId)) {
            throw new AccessDeniedException(String.format("Пользователь %d не является владельцем вещи %d", userId, itemId));
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return toItemDto(item);
    }

    public ItemDto getById(Long id) {
        Item item = items.get(id);
        if (item == null) throw new RuntimeException("Вещь не найдена");
        return toItemDto(item);
    }

    public List<ItemDto> getOwnerItems(Long userId) {
        if (!userService.existsById(userId)) {
            throw new RuntimeException(String.format("Пользователь с ID %d не найден", userId));
        }
        return items.values().stream().filter(item -> item.getOwnerId().equals(userId)).map(this::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String query = text.toLowerCase();
        return items.values().stream().filter(Item::getAvailable).filter(item -> item.getName().toLowerCase().contains(query) || item.getDescription().toLowerCase().contains(query)).map(this::toItemDto).collect(Collectors.toList());
    }

    private ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    private void validate(ItemDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Описание не может быть пустым");
        }
        if (dto.getAvailable() == null) {
            throw new IllegalArgumentException("Поле 'available' обязательно");
        }
    }
}