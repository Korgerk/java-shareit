package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDto; // Импортируем DTO из gateway
import java.util.Map;

@Component
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    // Конструктор, принимающий RestTemplate и serverUrl
    public UserClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest, serverUrl); // Вызываем конструктор BaseClient, передаем RestTemplate и serverUrl
    }

    public ResponseEntity<Object> create(UserDto userDto) { // Принимаем DTO из gateway
        return post(API_PREFIX, null, userDto); // Вызываем метод из BaseClient, передаем путь и тело
    }

    public ResponseEntity<Object> update(Long id, UserDto userDto) {
        String path = API_PREFIX + "/" + id;
        return patch(path, null, null, userDto); // Вызываем метод из BaseClient
    }

    public ResponseEntity<Object> getById(Long id) {
        String path = API_PREFIX + "/" + id;
        return get(path, null); // Вызываем метод из BaseClient
    }

    public ResponseEntity<Object> getAll() {
        return get(API_PREFIX, null); // Вызываем метод из BaseClient
    }

    public ResponseEntity<Object> delete(Long id) {
        String path = API_PREFIX + "/" + id;
        return delete(path, null); // Вызываем метод из BaseClient
    }
}