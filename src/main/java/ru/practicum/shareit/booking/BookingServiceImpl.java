package ru.practicum.shareit.booking;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import ru.practicum.shareit.expectation.ConflictException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + bookingDto.getItemId()));

        if (item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Owner cannot book their own item.");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available for booking.");
        }

        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("Start and end dates cannot be null.");
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Start date must be before end date.");
        }

        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Start date cannot be equal to end date.");
        }

        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Start and end dates must be in the future.");
        }

        Booking booking = new Booking(bookingDto.getStart(), bookingDto.getEnd(), item, booker, BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        return mapToBookingDto(savedBooking);
    }

    @Override
    public BookingDto approveOrRejectBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new SecurityException("Only the owner of the item can approve/reject the booking.");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ConflictException("Booking status is not WAITING and cannot be approved/rejected.");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return mapToBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new SecurityException("User is not the booker or owner of the item for this booking.");
        }

        return mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, BookingState state, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId, pageable).getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStatusIsCurrent(userId, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), now, pageable).getContent();
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, now, pageable).getContent();
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartAsc(userId, now, pageable).getContent();
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable).getContent();
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable).getContent();
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(this::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookingsByOwnerItems(Long userId, BookingState state, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId, pageable).getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStatusIsCurrent(userId, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), now, pageable).getContent();
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, now, pageable).getContent();
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, now, pageable).getContent();
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable).getContent();
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable).getContent();
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(this::mapToBookingDto)
                .collect(Collectors.toList());
    }

    private BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }
}