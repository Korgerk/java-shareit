package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')) AND i.available = true")
    List<Item> searchByText(@Param("text") String text);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status IN :statuses AND b.end < :now ORDER BY b.end DESC")
    Page<Booking> findLastBookingsByItem(@Param("itemId") Long itemId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status IN :statuses AND b.start > :now ORDER BY b.start ASC")
    Page<Booking> findNextBookingsByItem(@Param("itemId") Long itemId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item = :item AND b.booker.id = :bookerId AND b.status IN :statuses AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findByItemAndBookerAndStatusInAndEndIsBefore(@Param("item") Item item, @Param("bookerId") Long bookerId, @Param("statuses") List<BookingStatus> statuses, @Param("now") LocalDateTime now);
}