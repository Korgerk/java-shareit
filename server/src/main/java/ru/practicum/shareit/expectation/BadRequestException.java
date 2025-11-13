package ru.practicum.shareit.expectation;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}