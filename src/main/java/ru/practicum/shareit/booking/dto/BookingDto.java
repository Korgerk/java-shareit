package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

public class BookingDto {
    private Long id;

    @FutureOrPresent(message = "Start date must be in the future or present")
    @NotNull(message = "Start date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @Future(message = "End date must be in the future")
    @NotNull(message = "End date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    @NotNull(message = "Item ID cannot be null")
    private Long itemId;

    private BookingStatus status;

    private Long bookerId;

    public BookingDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Long getBookerId() {
        return bookerId;
    }

    public void setBookerId(Long bookerId) {
        this.bookerId = bookerId;
    }
}