package ru.practicum.shareit.client;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) throws BadRequestException {
        return post(API_PREFIX, userId, itemDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) throws BadRequestException {
        return patch(API_PREFIX + "/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getById(Long userId, Long itemId, MultiValueMap<String, String> params) throws BadRequestException {
        return get(API_PREFIX + "/" + itemId, userId, params);
    }

    public ResponseEntity<Object> getOwnerItems(Long userId, MultiValueMap<String, String> params) throws BadRequestException {
        return get(API_PREFIX, userId, params);
    }

    public ResponseEntity<Object> search(MultiValueMap<String, String> params) throws BadRequestException {
        return get(API_PREFIX + "/search", null, params);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, String text) throws BadRequestException {
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