package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    public UserClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest);
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        return post(API_PREFIX, null, userDto);
    }

    public ResponseEntity<Object> update(Long id, UserDto userDto) {
        String path = API_PREFIX + "/" + id;
        return patch(path, null, null, userDto);
    }

    public ResponseEntity<Object> getById(Long id) {
        String path = API_PREFIX + "/" + id;
        return get(path, null);
    }

    public ResponseEntity<Object> getAll() {
        return get(API_PREFIX, null);
    }

    public ResponseEntity<Object> delete(Long id) {
        String path = API_PREFIX + "/" + id;
        return delete(path, null);
    }
}