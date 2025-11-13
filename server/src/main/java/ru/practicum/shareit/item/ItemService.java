package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingService bookingService;

    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        // Проверка пользователя
        userService.getById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner((User) userService.getById(userId).getBody());
        Item savedItem = itemRepository.save(item);
        return ResponseEntity.ok(ItemMapper.toItemDto(savedItem));
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        if (!item.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User is not the owner of the item");
        }

        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(item);
        return ResponseEntity.ok(ItemMapper.toItemDto(updatedItem));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        ItemDto itemDto = ItemMapper.toItemDto(item);
        // Заполнить lastBooking, nextBooking, comments через BookingService и CommentService
        itemDto.setLastBooking((BookingShortDto) bookingService.getLastBookingForItem(itemId).getBody());
        itemDto.setNextBooking((BookingShortDto) bookingService.getNextBookingForItem(itemId).getBody());
        itemDto.setComments((List<CommentDto>) getCommentsForItem(itemId).getBody());

        return ResponseEntity.ok(itemDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getOwnerItems(Long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);
        List<ItemDto> itemDtos = items.stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.toItemDto(item);
                    itemDto.setLastBooking((BookingShortDto) bookingService.getLastBookingForItem(item.getId()).getBody());
                    itemDto.setNextBooking((BookingShortDto) bookingService.getNextBookingForItem(item.getId()).getBody());
                    itemDto.setComments((List<CommentDto>) getCommentsForItem(item.getId()).getBody());
                    return itemDto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemDtos);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> search(String text, Integer from, Integer size) {
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<Item> items = itemRepository.searchByText(text, from, size);
        List<ItemDto> itemDtos = items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemDtos);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        // Проверить, что пользователь бронировал вещь
        // ...
        Comment comment = ItemMapper.toComment(commentDto);
        comment.setAuthor((User) userService.getById(userId).getBody());
        comment.setItem(itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found")));
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(ItemMapper.toCommentDto(savedComment));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getCommentsForItem(Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentDto> commentDtos = comments.stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentDtos);
    }
}