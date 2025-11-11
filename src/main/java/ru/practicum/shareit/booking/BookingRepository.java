package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item = :item AND b.status IN :statuses ORDER BY b.start DESC")
    List<Booking> findLastBookingsByItem(@Param("item") Item item, @Param("statuses") List<BookingStatus> statuses, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item = :item AND b.status IN :statuses AND b.start > :now ORDER BY b.start ASC")
    List<Booking> findNextBookingsByItem(@Param("item") Item item, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start < :now AND b.status = 'APPROVED' ORDER BY b.start DESC")
    List<Booking> findLastBookingsByItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :now AND b.status = 'APPROVED' ORDER BY b.start ASC")
    List<Booking> findNextBookingsByItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);
}