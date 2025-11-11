package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBooker_IdOrderByStartDesc(Long userId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :userId ORDER BY b.start DESC")
    Page<Booking> findByItemOwner_IdOrderByStartDesc(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start < :now AND b.end > :now AND b.status = 'APPROVED'")
    List<Booking> findCurrentBookingsForItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :now AND b.status = 'APPROVED' ORDER BY b.start ASC")
    List<Booking> findFutureBookingsForItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.end < :now AND b.status = 'APPROVED' ORDER BY b.end DESC")
    List<Booking> findPastBookingsForItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.start < :now AND b.end > :now")
    List<Booking> findCurrentBookingsByBooker(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.start > :now")
    List<Booking> findFutureBookingsByBooker(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.end < :now")
    List<Booking> findPastBookingsByBooker(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :userId AND b.start < :now AND b.end > :now")
    List<Booking> findCurrentBookingsByOwner(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :userId AND b.start > :now")
    List<Booking> findFutureBookingsByOwner(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :userId AND b.end < :now")
    List<Booking> findPastBookingsByOwner(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    Page<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long userId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItemOwner_IdAndStatusOrderByStartDesc(Long userId, BookingStatus status, Pageable pageable);
}