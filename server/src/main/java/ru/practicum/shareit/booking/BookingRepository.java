package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId")
    List<Booking> findByItemOwnerId(Long ownerId, Pageable pageable);

    List<Booking> findByItem_IdAndStartIsBeforeAndStatusEquals(Long itemId, java.time.LocalDateTime end, BookingStatus status);
    List<Booking> findByItem_IdAndStartIsAfterAndStatusEquals(Long itemId, java.time.LocalDateTime start, BookingStatus status);
}