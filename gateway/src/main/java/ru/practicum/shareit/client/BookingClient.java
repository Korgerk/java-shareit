package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.dto.BookingDto;

import java.util.List;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, BookingDto bookingDto) {
        return post(API_PREFIX, userId, bookingDto);
    }

    public ResponseEntity<Object> updateStatus(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch(API_PREFIX + "/" + bookingId, userId, parameters, null);
    }

    public ResponseEntity<Object> getById(Long userId, Long bookingId, MultiValueMap<String, String> params) {
        // Преобразуем MultiValueMap в Map<String, Object> для передачи в BaseClient
        Map<String, Object> parameters = null;
        if (params != null) {
            parameters = new java.util.HashMap<>();
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                if (values != null && !values.isEmpty()) {
                    parameters.put(key, values.get(0)); // Берём первое значение, как в toSingleValueMap
                } else {
                    parameters.put(key, null);
                }
            }
        }
        return get(API_PREFIX + "/" + bookingId, userId, parameters); // Вызов основного метода get
    }

    public ResponseEntity<Object> getAllByBooker(Long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get(API_PREFIX, userId, parameters);
    }

    public ResponseEntity<Object> getAllByOwner(Long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get(API_PREFIX + "/owner", userId, parameters);
    }
}