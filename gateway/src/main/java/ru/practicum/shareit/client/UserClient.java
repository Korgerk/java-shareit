package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.dto.UserDto;

import java.util.List;
import java.util.Map;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        return post(API_PREFIX, null, userDto);
    }

    public ResponseEntity<Object> update(Long userId, UserDto userDto) {
        return patch(API_PREFIX + "/" + userId, userId, userDto);
    }

    public ResponseEntity<Object> getById(Long userId, MultiValueMap<String, String> params) {
        Map<String, Object> parameters = null;
        if (params != null) {
            parameters = new java.util.HashMap<>();
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                if (values != null && !values.isEmpty()) {
                    parameters.put(key, values.get(0));
                } else {
                    parameters.put(key, null);
                }
            }
        }
        return get(API_PREFIX + "/" + userId, userId, parameters);
    }

    public ResponseEntity<Object> getAll(MultiValueMap<String, String> params) {
        Map<String, Object> parameters = null;
        if (params != null) {
            parameters = new java.util.HashMap<>();
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                if (values != null && !values.isEmpty()) {
                    parameters.put(key, values.get(0));
                } else {
                    parameters.put(key, null);
                }
            }
        }
        return get(API_PREFIX, null, parameters);
    }

    public ResponseEntity<Object> delete(Long userId) {
        return delete(API_PREFIX + "/" + userId, userId);
    }
}