package ru.practicum.shareit.client;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest, serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        return post(API_PREFIX, userId, itemDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        String path = API_PREFIX + "/" + itemId;
        return patch(path, userId, null, itemDto);
    }

    public ResponseEntity<Object> getById(Long userId, Long itemId) {
        String path = API_PREFIX + "/" + itemId;
        return get(path, userId);
    }

    public ResponseEntity<Object> getOwnerItems(Long userId) {
        return get(API_PREFIX, userId);
    }

    public ResponseEntity<Object> search(String text, @PositiveOrZero Integer from, @Positive Integer size) {
        String path = API_PREFIX + "/search?text=" + (text == null ? "" : text);
        return get(path, null);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        String path = API_PREFIX + "/" + itemId + "/comment";
        return post(path, userId, commentDto);
    }
}