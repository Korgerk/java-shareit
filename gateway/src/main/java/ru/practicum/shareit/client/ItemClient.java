package ru.practicum.shareit.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Component
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        return post(API_PREFIX, userId, itemDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        String path = API_PREFIX + "/{itemId}";
        return patch(path, userId, Map.of("itemId", itemId), itemDto);
    }

    public ResponseEntity<Object> getById(Long userId, Long itemId) {
        String path = API_PREFIX + "/{itemId}";
        return get(path, userId, Map.of("itemId", itemId));
    }

    public ResponseEntity<Object> getOwnerItems(Long userId) {
        return get(API_PREFIX, userId);
    }

    public ResponseEntity<Object> search(String text, Integer from, Integer size) {
        String path = API_PREFIX + "/search?text={text}&from={from}&size={size}";
        Map<String, Object> parameters = Map.of("text", text == null ? "" : text, "from", from, "size", size);
        return get(path, null, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        String path = API_PREFIX + "/{itemId}/comment";
        return post(path, userId, commentDto);
    }
}