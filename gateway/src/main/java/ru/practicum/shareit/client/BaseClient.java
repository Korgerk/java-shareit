package ru.practicum.shareit.client;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

public class BaseClient {
    private final WebClient webClient;

    protected BaseClient(String serverUrl) {
        webClient = WebClient.builder().baseUrl(serverUrl).build();
    }

    protected ResponseEntity<Object> get(String path, Long userId, MultiValueMap<String, String> params) throws BadRequestException {
        return get(path, null, (MultiValueMap<String, String>) null);
    }

    protected ResponseEntity<Object> get(String path, Long userId, Map<String, Object> parameters) throws BadRequestException {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected ResponseEntity<Object> post(String path, Long userId, Object body) throws BadRequestException {
        return post(path, userId, null, body);
    }

    protected ResponseEntity<Object> post(String path, Long userId, Map<String, Object> parameters, Object body) throws BadRequestException {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> put(String path, Long userId, Object body) throws BadRequestException {
        return put(path, userId, null, body);
    }

    protected ResponseEntity<Object> put(String path, Long userId, Map<String, Object> parameters, Object body) throws BadRequestException {
        return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> patch(String path, Long userId, Object body) throws BadRequestException {
        return patch(path, userId, null, body);
    }

    protected ResponseEntity<Object> patch(String path, Long userId, Map<String, Object> parameters, Object body) throws BadRequestException {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path, Long userId) throws BadRequestException {
        return delete(path, userId, null);
    }

    protected ResponseEntity<Object> delete(String path, Long userId, Map<String, Object> parameters) throws BadRequestException {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, Map<String, Object> parameters, Object body) throws BadRequestException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);

        if (parameters != null) {
            parameters.forEach(builder::queryParam);
        }

        WebClient.RequestHeadersSpec<?> spec = webClient.method(method).uri(builder.build().toUri());

        Optional.ofNullable(userId).ifPresent(id -> spec.header("X-Sharer-User-Id", String.valueOf(id)));

        if (body != null) {
            ((WebClient.RequestBodySpec) spec).bodyValue(body);
        }

        try {
            return spec.retrieve().toEntity(Object.class).block();
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            throw new BadRequestException("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }
}