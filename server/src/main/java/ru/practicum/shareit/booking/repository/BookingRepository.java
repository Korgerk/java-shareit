package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserId(long userId);

    List<Booking> findAllByItem_OwnerId(long userId);

    @Query("SELECT b"
           + " FROM Booking b"
           + " WHERE b.item.id = :itemId"
           + " AND b.status = 'APPROVED'"
           + " AND b.end < :now"
           + " ORDER BY b.end DESC"
           + " LIMIT 1")
    Booking findLastBookingForItem(long itemId, Instant now);

    @Query("SELECT b "
           + " FROM Booking b"
           + " WHERE b.item.id = :itemId"
           + " AND b.status = 'APPROVED'"
           + " AND b.start > :now"
           + " ORDER BY b.start ASC"
           + " LIMIT 1")
    Booking findNextBookingForItem(long itemId, Instant now);

    Optional<Booking> findFirstByItemIdAndUserIdAndStatusAndEndBefore(long itemId,
                                                                      long userId,
                                                                      BookingStatus status,
                                                                      Instant instant);
}