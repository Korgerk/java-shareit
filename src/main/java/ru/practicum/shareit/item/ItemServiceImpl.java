package ru.practicum.shareit.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, CommentRepository commentRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Item item = new Item(itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), owner);
        item.setRequestId(itemDto.getRequestId());
        Item savedItem = itemRepository.save(item);
        return mapToItemDto(savedItem, false, userId);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item existingItem = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new SecurityException("User is not the owner of the item.");
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequestId() != null) {
            existingItem.setRequestId(itemDto.getRequestId());
        }

        Item updatedItem = itemRepository.save(existingItem);
        return mapToItemDto(updatedItem, false, userId);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));
        return mapToItemDto(item, true, userId);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());
        return itemRepository.findByOwnerId(userId, pageable).stream().map(item -> mapToItemDto(item, true, userId)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text, Integer from, Integer size) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());
        return itemRepository.findAll(pageable).stream().filter(item -> item.getAvailable() && item.getName().toLowerCase().contains(text.toLowerCase())).map(this::mapToItemDtoWithoutBookings).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long itemId, Long userId, String text) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

        User author = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (item.getOwner().getId().equals(userId)) {
            throw new SecurityException("Owner cannot leave a comment on their own item.");
        }

        List<Booking> pastBookings = bookingRepository.findByItemAndStatusInOrderByStartDesc(item, List.of(BookingStatus.APPROVED)).stream().filter(booking -> booking.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());

        boolean hasPastBooking = pastBookings.stream().anyMatch(booking -> booking.getBooker().getId().equals(userId));

        if (!hasPastBooking) {
            throw new IllegalStateException("User has not booked this item before.");
        }

        Comment comment = new Comment(text, item, author, LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return mapToCommentDto(savedComment);
    }

    private ItemDto mapToItemDto(Item item, boolean includeBookings, Long userId) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwnerId(item.getOwner().getId());
        itemDto.setRequestId(item.getRequestId());

        if (includeBookings && item.getOwner().getId().equals(userId)) {
            List<Booking> pastBookings = bookingRepository.findLastBookingsByItem(item.getId(), LocalDateTime.now());
            if (!pastBookings.isEmpty()) {
                Booking lastBooking = pastBookings.get(0);
                ru.practicum.shareit.booking.dto.BookingShortDto lastBookingDto = new ru.practicum.shareit.booking.dto.BookingShortDto();
                lastBookingDto.setId(lastBooking.getId());
                lastBookingDto.setStart(lastBooking.getStart());
                lastBookingDto.setEnd(lastBooking.getEnd());
                lastBookingDto.setBookerId(lastBooking.getBooker().getId());
                lastBookingDto.setStatus(lastBooking.getStatus());
                itemDto.setLastBooking(lastBookingDto);
            }

            List<Booking> futureBookings = bookingRepository.findNextBookingsByItem(item.getId(), LocalDateTime.now());
            if (!futureBookings.isEmpty()) {
                Booking nextBooking = futureBookings.get(0);
                ru.practicum.shareit.booking.dto.BookingShortDto nextBookingDto = new ru.practicum.shareit.booking.dto.BookingShortDto();
                nextBookingDto.setId(nextBooking.getId());
                nextBookingDto.setStart(nextBooking.getStart());
                nextBookingDto.setEnd(nextBooking.getEnd());
                nextBookingDto.setBookerId(nextBooking.getBooker().getId());
                nextBookingDto.setStatus(nextBooking.getStatus());
                itemDto.setNextBooking(nextBookingDto);
            }
        }

        List<CommentDto> commentDtos = commentRepository.findByItemId(item.getId()).stream().map(this::mapToCommentDto).collect(Collectors.toList());
        itemDto.setComments(commentDtos);

        return itemDto;
    }

    private ItemDto mapToItemDtoWithoutBookings(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwnerId(item.getOwner().getId());
        itemDto.setRequestId(item.getRequestId());
        List<CommentDto> commentDtos = commentRepository.findByItemId(item.getId()).stream().map(this::mapToCommentDto).collect(Collectors.toList());
        itemDto.setComments(commentDtos);
        return itemDto;
    }

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }
}