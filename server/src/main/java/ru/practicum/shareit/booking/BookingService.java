package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public ResponseEntity<Object> createBooking(Long userId, BookingDto bookingDto) {
        userService.getById(userId);
        itemService.getById(userId, bookingDto.getItemId());

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(userService.getById(userId).getBody());
        booking.setItem(itemService.getById(userId, bookingDto.getItemId()).getBody());
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(BookingMapper.toBookingDto(savedBooking));
    }

    public ResponseEntity<Object> updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        Long ownerId = booking.getItem().getOwner().getId();
        if (!ownerId.equals(userId)) {
            throw new RuntimeException("User is not the owner of the item for this booking");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(BookingMapper.toBookingDto(updatedBooking));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new RuntimeException("User is not the booker or owner of the item for this booking");
        }

        return ResponseEntity.ok(BookingMapper.toBookingDto(booking));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getBookingsByBookerId(Long userId, String state, Integer from, Integer size) {
        // Проверка пользователя
        userService.getById(userId);

        List<Booking> bookings = bookingRepository.findByBookerId(userId, from, size);
        List<BookingDto> bookingDtos = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingDtos);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getBookingsByOwnerId(Long userId, String state, Integer from, Integer size) {
        // Проверка пользователя
        userService.getById(userId);

        List<Booking> bookings = bookingRepository.findByItemOwnerId(userId, from, size);
        List<BookingDto> bookingDtos = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingDtos);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getLastBookingForItem(Long itemId) {
        // Логика получения последнего подтвержденного бронирования для вещи
        // ...
        return ResponseEntity.ok(null); // Заглушка
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getNextBookingForItem(Long itemId) {
        // Логика получения следующего подтвержденного бронирования для вещи
        // ...
        return ResponseEntity.ok(null); // Заглушка
    }
}