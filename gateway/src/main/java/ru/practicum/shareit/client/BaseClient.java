// BaseClient.java
package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public abstract class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path, Long userId, MultiValueMap<String, String> parameters) {
        return get(path, (Long) null, (MultiValueMap<String, String>) null);
    }

    protected ResponseEntity<Object> get(String path, Long userId) {
        return get(path, userId, (MultiValueMap<String, String>) null);
    }

    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected ResponseEntity<Object> post(String path) {
        return post(path, null, null);
    }

    protected ResponseEntity<Object> post(String path, Long userId, Object body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, null, body);
    }

    protected ResponseEntity<Object> patch(String path, Long userId) {
        return patch(path, userId, null, null);
    }

    protected ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return patch(path, userId, parameters, null);
    }

    protected ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, Object body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path, Long userId) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, null, null);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String url, Long userId, @Nullable Map<String, Object> parameters, @Nullable Object body) {
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