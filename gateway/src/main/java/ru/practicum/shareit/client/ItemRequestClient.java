package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest);
    }

    public ResponseEntity<Object> create(Long userId, ItemRequestDto itemRequestDto) {
        return post(API_PREFIX, userId, itemRequestDto);
    }

    public ResponseEntity<Object> getAllByRequestor(Long userId) {
        return get(API_PREFIX, userId);
    }

    public ResponseEntity<Object> getAllRequests(Long userId, Integer from, Integer size) {
        String path = API_PREFIX + "/all?from=" + from + "&size=" + size;
        return get(path, userId);
    }

    public ResponseEntity<Object> getById(Long userId, Long requestId) {
        String path = API_PREFIX + "/" + requestId;
        return get(path, userId);
    }
}