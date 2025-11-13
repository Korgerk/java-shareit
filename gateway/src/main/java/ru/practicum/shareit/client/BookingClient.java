package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX)).requestFactory((settings) -> new HttpComponentsClientHttpRequestFactory()).build());
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId, userId, parameters, null);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByBookerId(Long userId, BookingStatus state, Integer from, Integer size) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("state", state.name());
        parameters.add("from", from.toString());
        parameters.add("size", size.toString());
        return get("", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwnerId(Long userId, BookingStatus state, Integer from, Integer size) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("state", state.name());
        parameters.add("from", from.toString());
        parameters.add("size", size.toString());
        return get("/owner", userId, parameters);
    }
}