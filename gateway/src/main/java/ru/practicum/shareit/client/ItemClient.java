package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.dto.ItemDto;

import java.util.List;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        return post(API_PREFIX, userId, itemDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        return patch(API_PREFIX + "/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getById(Long userId, Long itemId, MultiValueMap<String, String> params) {
        // Преобразуем MultiValueMap в Map<String, Object>
        Map<String, Object> parameters = null;
        if (params != null) {
            parameters = new java.util.HashMap<>();
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                if (values != null && !values.isEmpty()) {
                    parameters.put(key, values.get(0));
                } else {
                    parameters.put(key, null);
                }
            }
        }
        return get(API_PREFIX + "/" + itemId, userId, parameters); // Вызов основного метода get
    }

    public ResponseEntity<Object> getOwnerItems(Long userId, MultiValueMap<String, String> params) {
        // Преобразуем MultiValueMap в Map<String, Object>
        Map<String, Object> parameters = null;
        if (params != null) {
            parameters = new java.util.HashMap<>();
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                if (values != null && !values.isEmpty()) {
                    parameters.put(key, values.get(0));
                } else {
                    parameters.put(key, null);
                }
            }
        }
        return get(API_PREFIX, userId, parameters); // Вызов основного метода get
    }

    public ResponseEntity<Object> search(MultiValueMap<String, String> params) {
        // Преобразуем MultiValueMap в Map<String, Object>
        Map<String, Object> parameters = null;
        if (params != null) {
            parameters = new java.util.HashMap<>();
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                if (values != null && !values.isEmpty()) {
                    parameters.put(key, values.get(0));
                } else {
                    parameters.put(key, null);
                }
            }
        }
        return get(API_PREFIX + "/search", null, parameters); // userId не нужен для поиска
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, String text) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(text);
        return post(API_PREFIX + "/" + itemId + "/comment", userId, commentDto);
    }

    public static class CommentDto {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}