package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum BookingStatus {
    WAITING, APPROVED, REJECTED, CANCELLED;

    public static Optional<BookingStatus> from(String value) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}