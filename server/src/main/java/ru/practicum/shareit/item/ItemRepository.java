package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByOwner_Id(Long ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.available = true AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> search(@Param("text") String text);

    List<Item> findByRequestId(Long requestId);

    List<Item> findByRequestIdIn(List<Long> requestIds);
}