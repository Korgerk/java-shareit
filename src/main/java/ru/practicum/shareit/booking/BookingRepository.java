package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "WHERE b.booker.id = :userId " + "AND b.status IN :statuses " + "AND b.start < :now " + "AND b.end > :now " + "ORDER BY b.start DESC")
    Page<Booking> findByBookerIdAndStatusIsCurrent(@Param("userId") Long userId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "WHERE b.booker.id = :userId " + "AND b.start > :now " + "ORDER BY b.start ASC")
    Page<Booking> findByBookerIdAndStartIsAfter(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "WHERE b.booker.id = :userId " + "AND b.end < :now " + "ORDER BY b.start DESC")
    Page<Booking> findByBookerIdAndEndIsBefore(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);

    Page<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "JOIN b.item i " + "WHERE i.owner.id = :userId " + "AND b.status IN :statuses " + "AND b.start < :now " + "AND b.end > :now " + "ORDER BY b.start DESC")
    Page<Booking> findByItemOwnerIdAndStatusIsCurrent(@Param("userId") Long userId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "JOIN b.item i " + "WHERE i.owner.id = :userId " + "AND b.start > :now " + "ORDER BY b.start ASC")
    Page<Booking> findByItemOwnerIdAndStartIsAfter(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "JOIN b.item i " + "WHERE i.owner.id = :userId " + "AND b.end < :now " + "ORDER BY b.start DESC")
    Page<Booking> findByItemOwnerIdAndEndIsBefore(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status, Pageable pageable);

    List<Booking> findByItemAndStatusInOrderByStartDesc(Item item, List<BookingStatus> statuses);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start < :now AND b.status = 'APPROVED' ORDER BY b.start DESC")
    List<Booking> findLastBookingsByItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :now AND b.status = 'APPROVED' ORDER BY b.start ASC")
    List<Booking> findNextBookingsByItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);
}