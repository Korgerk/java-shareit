package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.client.BaseClient;


@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, CreateBookingDto booking) {
        if (booking.getStart().equals(booking.getEnd())
            || booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("end should be strongly after start");
        }
        return post("", userId, booking);
    }

    public ResponseEntity<Object> approveBooking(long userId, long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBooking(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getUserBookings(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getOwnerBookings(long ownerId) {
        return get("/owner", ownerId);
    }
}