package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requestor.id != :userId ORDER BY ir.created DESC")
    Page<ItemRequest> findAllByRequestorIdNot(@Param("userId") Long userId, Pageable pageable);
}