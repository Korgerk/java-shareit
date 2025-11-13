package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingDto;

@Component
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest, serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, BookingDto bookingDto) {
        return post(API_PREFIX, userId, bookingDto);
    }

    public ResponseEntity<Object> updateStatus(Long userId, Long bookingId, Boolean approved) {
        String path = API_PREFIX + "/" + bookingId + "?approved=" + approved;
        return patch(path, userId);
    }

    public ResponseEntity<Object> getById(Long userId, Long bookingId) {
        String path = API_PREFIX + "/" + bookingId;
        return get(path, userId);
    }

    public ResponseEntity<Object> getAllByBooker(Long userId, String state, Integer from, Integer size) {
        String path = API_PREFIX + "?state=" + state + "&from=" + from + "&size=" + size;
        return get(path, userId);
    }

    public ResponseEntity<Object> getAllByOwner(Long userId, String state, Integer from, Integer size) {
        String path = API_PREFIX + "/owner?state=" + state + "&from=" + from + "&size=" + size;
        return get(path, userId);
    }
}