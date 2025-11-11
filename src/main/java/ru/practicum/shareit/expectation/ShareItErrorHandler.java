package ru.practicum.shareit.expectation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ShareItErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        // Важно: возвращаем 400, так как это ошибка валидации DTO
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        // Важно: возвращаем 400, так как это ошибка валидации DTO (например, @Positive)
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        // Многие сервисы выбрасывают IllegalArgumentException для несуществующих сущностей
        // Возвращаем 404, если это ошибка "not found"
        if (e.getMessage().contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
        // Иначе возвращаем 400 для других IllegalArgumentException
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException e) {
        // ТЗ требует 404, если пользователь не владелец вещи при обновлении
        // Вместо SecurityException, бросаем IllegalArgumentException в сервисе
        // Этот обработчик может остаться, но не будет использоваться для этой цели
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        // Возвращаем 400, так как заголовок обязателен
        return ResponseEntity.badRequest().body(Map.of("error", "Required header '" + e.getHeaderName() + "' is missing"));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Throwable cause = e.getRootCause();
        if (cause instanceof SQLException) {
            SQLException sqlEx = (SQLException) cause;
            String message = sqlEx.getMessage();
            // Проверяем сообщение об ошибке на наличие ключа уникальности
            // Имя ограничения может зависеть от БД и DDL, но часто содержит "email"
            if (message != null && message.contains("email")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists"));
            }
            // Можно добавить другие проверки на основе message
        }
        // Если не удалось определить конкретную причину, возвращаем общий ответ
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Data integrity violation: " + e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        e.printStackTrace();
        // Не рекомендуется возвращать e.getMessage() в продакшене, но для отладки подходит
        // Важно: возвращаем 500 для внутренних ошибок
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred: " + e.getClass().getSimpleName()));
    }
}