package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public abstract class BaseClient {
    protected final RestTemplate rest;
    private final String serverUrl;

    public BaseClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        this.rest = rest;
        this.serverUrl = serverUrl;
    }

    private String buildUrl(String path) {
        String baseUrl = this.serverUrl.endsWith("/") ? this.serverUrl.substring(0, this.serverUrl.length() - 1) : this.serverUrl;
        String targetPath = path.startsWith("/") ? path : "/" + path;
        return baseUrl + targetPath;
    }

    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    protected ResponseEntity<Object> get(String path, Long userId) {
        return get(path, userId, null);
    }

    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, buildUrl(path), userId, parameters, null);
    }

    protected ResponseEntity<Object> post(String path) {
        return post(path, null, null);
    }

    protected ResponseEntity<Object> post(String path, Long userId, Object body) {
        return makeAndSendRequest(HttpMethod.POST, buildUrl(path), userId, null, body);
    }

    protected ResponseEntity<Object> patch(String path, Long userId) {
        return patch(path, userId, null, null);
    }

    protected ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return patch(path, userId, parameters, null);
    }

    protected ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, Object body) {
        return makeAndSendRequest(HttpMethod.PATCH, buildUrl(path), userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path, Long userId) {
        return makeAndSendRequest(HttpMethod.DELETE, buildUrl(path), userId, null, null);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String url, Long userId, @Nullable Map<String, Object> parameters, @Nullable Object body) { // <-- Параметр url вместо path
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(url, method, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(url, method, requestEntity, Object.class);
            }
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return serverResponse;
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }
}