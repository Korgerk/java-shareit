package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.expectation.AccessDeniedException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    public static final String ERROR_MESSAGE_END_AFTER_START = "Дата окончания бронирования должна быть позже даты начала.";
    private static final String ERROR_MESSAGE_CANNOT_BOOK_OWN = "Владелец не может забронировать свою вещь.";
    private static final String ERROR_MESSAGE_ITEM_NOT_FOUND = "Вещь с ID %d не найдена";
    private static final String ERROR_MESSAGE_ITEM_UNAVAILABLE = "Вещь недоступна для бронирования.";
    private static final String ERROR_MESSAGE_START_IN_PAST = "Дата начала бронирования не может быть в прошлом.";
    private static final String ERROR_MESSAGE_BOOKING_NOT_FOUND = "Бронирование с ID %d не найдено";
    private static final String ERROR_MESSAGE_OWNER_ONLY_APPROVE = "Только владелец вещи может подтвердить бронирование.";
    private static final String ERROR_MESSAGE_AUTHOR_OR_OWNER_ONLY = "Только автор бронирования или владелец вещи может просматривать бронирование.";
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public BookingDto create(BookingDto dto, Long userId) {
        User booker = userService.getById(userId);
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new RuntimeException(String.format(ERROR_MESSAGE_ITEM_NOT_FOUND, dto.getItemId())));

        if (!item.getAvailable()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ITEM_UNAVAILABLE);
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_CANNOT_BOOK_OWN);
        }

        LocalDateTime start = dto.getStart();
        LocalDateTime end = dto.getEnd();
        if (start == null || end == null) {
            throw new IllegalArgumentException("Дата начала и окончания бронирования обязательны.");
        }
        if (start.isBefore(LocalDateTime.now().minusSeconds(1))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_START_IN_PAST);
        }
        if (end.isBefore(start) || start.isEqual(end) || Duration.between(start, end).getSeconds() < 1) {
            throw new IllegalArgumentException(ERROR_MESSAGE_END_AFTER_START);
        }

        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
        return toBookingDto(booking);
    }

    public BookingDto updateStatus(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException(String.format(ERROR_MESSAGE_BOOKING_NOT_FOUND, bookingId)));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException(ERROR_MESSAGE_OWNER_ONLY_APPROVE);
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        booking = bookingRepository.save(booking);
        return toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    public BookingDto getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException(String.format(ERROR_MESSAGE_BOOKING_NOT_FOUND, bookingId)));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException(ERROR_MESSAGE_AUTHOR_OR_OWNER_ONLY);
        }

        return toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getAllByBooker(Long userId, String state, int from, int size) {
        userService.getById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageable = PageRequest.of(from / size, size, sort);

        List<Booking> bookings;
        switch (state) {
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookingsByBooker(userId, LocalDateTime.now());
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookingsByBooker(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookingsByBooker(userId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable).getContent();
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable).getContent();
                break;
            case "ALL":
            default:
                bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId, pageable).getContent();
        }
        return bookings.stream().map(this::toBookingDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getAllByOwner(Long userId, String state, int from, int size) {
        userService.getById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageable = PageRequest.of(from / size, size, sort);

        List<Booking> bookings;
        switch (state) {
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookingsByOwner(userId, LocalDateTime.now());
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookingsByOwner(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookingsByOwner(userId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingRepository.findByItemOwner_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable).getContent();
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItemOwner_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable).getContent();
                break;
            case "ALL":
            default:
                bookings = bookingRepository.findByItemOwner_IdOrderByStartDesc(userId, pageable).getContent();
        }
        return bookings.stream().map(this::toBookingDto).collect(Collectors.toList());
    }

    private BookingDto toBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        BookingItemDto itemDto = new BookingItemDto();
        itemDto.setId(booking.getItem().getId());
        itemDto.setName(booking.getItem().getName());
        dto.setItem(itemDto);
        BookingUserDto bookerDto = new BookingUserDto();
        bookerDto.setId(booking.getBooker().getId());
        dto.setBooker(bookerDto);
        dto.setStatus(booking.getStatus());
        return dto;
    }
}