package ru.practicum.shareit.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.model.User;


import java.util.Map;

public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    public UserClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> create(User user) {
        return post(API_PREFIX, null, user);
    }

    public ResponseEntity<Object> update(Long id, User user) {
        String path = API_PREFIX + "/{id}";
        return patch(path, null, Map.of("id", id), user);
    }

    public ResponseEntity<Object> getAll() {
        return get(API_PREFIX);
    }

    public ResponseEntity<Object> getById(Long id) {
        String path = API_PREFIX + "/{id}";
        return get(path, null, Map.of("id", id));
    }

    public ResponseEntity<Object> delete(Long id) {
        String path = API_PREFIX + "/{id}";
        return rest.exchange(path, org.springframework.http.HttpMethod.DELETE, null, Object.class, Map.of("id", id));
    }
}