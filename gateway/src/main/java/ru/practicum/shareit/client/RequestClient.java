package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(long userId, CreateItemRequestDto createItemRequestDto) {
        return post("", userId, createItemRequestDto);
    }

    public ResponseEntity<Object> getUserItemRequests(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getOtherUsersItemRequests(long userId) {
        return get("/all", userId);
    }

    public ResponseEntity<Object> getItemRequest(long requestId) {
        return get("/" + requestId);
    }
}