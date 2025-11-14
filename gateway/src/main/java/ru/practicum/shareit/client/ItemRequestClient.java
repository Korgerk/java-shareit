package ru.practicum.shareit.client;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, ItemRequestDto requestDto) throws BadRequestException {
        return post(API_PREFIX, userId, requestDto);
    }

    public ResponseEntity<Object> getById(Long userId, Long requestId, MultiValueMap<String, String> params) throws BadRequestException {
        return get(API_PREFIX + "/" + requestId, userId, params);
    }

    public ResponseEntity<Object> getAllByRequestor(Long userId, MultiValueMap<String, String> params) throws BadRequestException {
        return get(API_PREFIX, userId, params);
    }

    public ResponseEntity<Object> getAll(Long userId, Integer from, Integer size) throws BadRequestException {
        Map<String, Object> parameters = Map.of("from", from, "size", size);
        return get(API_PREFIX + "/all", userId, parameters);
    }
}