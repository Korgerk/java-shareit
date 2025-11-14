package ru.practicum.shareit.client;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.dto.BookingDto;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, BookingDto bookingDto) throws BadRequestException {
        return post(API_PREFIX, userId, bookingDto);
    }

    public ResponseEntity<Object> updateStatus(Long userId, Long bookingId, Boolean approved) throws BadRequestException {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch(API_PREFIX + "/" + bookingId, userId, parameters, null);
    }

    public ResponseEntity<Object> getById(Long userId, Long bookingId, MultiValueMap<String, String> params) throws BadRequestException {
        return get(API_PREFIX + "/" + bookingId, userId, params);
    }

    public ResponseEntity<Object> getAllByBooker(Long userId, String state, Integer from, Integer size) throws BadRequestException {
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get(API_PREFIX, userId, parameters);
    }

    public ResponseEntity<Object> getAllByOwner(Long userId, String state, Integer from, Integer size) throws BadRequestException {
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get(API_PREFIX + "/owner", userId, parameters);
    }
}