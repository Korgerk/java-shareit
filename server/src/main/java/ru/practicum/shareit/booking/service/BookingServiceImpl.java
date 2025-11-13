package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.expectation.BadRequestException;
import ru.practicum.shareit.expectation.EntityNotFoundException;
import ru.practicum.shareit.expectation.ForbiddenException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto addBooking(long userId, CreateBookingDto createBooking) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user not found"));

        Item item = itemRepository.findById(createBooking.getItemId()).orElseThrow(() -> new EntityNotFoundException("item not found"));

        if (userId == item.getOwner().getId()) {
            throw new BadRequestException("booking your own item");
        }

        if (!item.isAvailable()) {
            throw new BadRequestException("item is not available");
        }

        Booking booking = bookingMapper.mapToBooking(createBooking);
        booking.setItem(item);
        booking.setUser(user);
        booking.setStatus(BookingStatus.WAITING);

        return bookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approveBooking(long userId, long bookingId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("booking not found"));

        if (booking.getItem().getOwner().getId() != userId) {
            throw new ForbiddenException("you have no rights to approve booking");
        }

        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBooking(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("booking not found"));

        if (booking.getUser().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ForbiddenException("booking is available only for item owner or booker");
        }

        return bookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getUserBookings(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user not found"));

        return bookingRepository.findAllByUserId(userId).stream().map(bookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getOwnerBookings(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user not found"));

        return bookingRepository.findAllByItem_OwnerId(userId).stream().map(bookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

}