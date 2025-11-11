package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.status IN :statuses AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    Page<Booking> findByBookerIdAndStatusIsCurrent(@Param("userId") Long userId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    Page<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsAfterOrderByStartAsc(Long bookerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :userId AND b.status IN :statuses AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    Page<Booking> findByItemOwnerIdAndStatusIsCurrent(@Param("userId") Long userId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartAsc(Long ownerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = 'APPROVED' AND b.end < :now ORDER BY b.end DESC")
    List<Booking> findLastApprovedBookingsByItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = 'APPROVED' AND b.start > :now ORDER BY b.start ASC")
    List<Booking> findNextApprovedBookingsByItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    List<Booking> findByItemAndStatusInOrderByStartDesc(Item item, List<BookingStatus> statuses);

    Optional<Booking> findByIdAndBookerIdOrItemOwnerId(Long id, Long bookerId, Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item = :item AND b.status = 'APPROVED' AND b.end < :now ORDER BY b.end DESC")
    List<Booking> findLastApprovedBookingsByItem(@Param("item") Item item, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item = :item AND b.status = 'APPROVED' AND b.start > :now ORDER BY b.start ASC")
    List<Booking> findNextApprovedBookingsByItem(@Param("item") Item item, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status IN :statuses AND b.end < :now ORDER BY b.start DESC")
    Page<Booking> findLastBookingsByItem(@Param("itemId") Long itemId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status IN :statuses AND b.start > :now ORDER BY b.start ASC")
    Page<Booking> findNextBookingsByItem(@Param("itemId") Long itemId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item = :item AND b.booker.id = :bookerId AND b.status IN :statuses AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findByItemAndBookerAndStatusInAndEndIsBefore(@Param("item") Item item, @Param("bookerId") Long bookerId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :userId AND b.end < :now ORDER BY b.start DESC")
    Page<Booking> findByItemOwnerIdAndEndIsBefore(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :userId AND b.start > :now ORDER BY b.start ASC")
    Page<Booking> findByItemOwnerIdAndStartIsAfter(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);
}