package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i " + "JOIN i.owner u " + "LEFT JOIN Booking b ON b.item.id = i.id AND b.status = 'APPROVED' " + "WHERE u.id = :ownerId " + "ORDER BY b.start DESC")
    Page<Item> findItemsWithBookingsByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

    List<Item> findByRequestId(Long requestId);
}