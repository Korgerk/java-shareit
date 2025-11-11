package ru.practicum.shareit.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
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
        return mapToItemDto(savedItem);
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
        return mapToItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

        ItemDto itemDto = mapToItemDto(item);

        if (item.getOwner().getId().equals(userId)) {
            Pageable limitOne = PageRequest.of(0, 1, Sort.by("start").descending());
            Pageable limitOneAsc = PageRequest.of(0, 1, Sort.by("start").ascending());

            List<Booking> pastBookings = bookingRepository.findLastBookingsByItem(item.getId(), List.of(BookingStatus.APPROVED), LocalDateTime.now(), limitOne).getContent(); // .getContent() извлекает List<T> из Page<T>

            if (!pastBookings.isEmpty()) {
                Booking lastBooking = pastBookings.get(0);
                itemDto.setLastBooking(mapToBookingShortDto(lastBooking));
            }

            List<Booking> futureBookings = bookingRepository.findNextBookingsByItem(item.getId(), List.of(BookingStatus.APPROVED), LocalDateTime.now(), limitOneAsc).getContent(); // .getContent() извлекает List<T> из Page<T>

            if (!futureBookings.isEmpty()) {
                Booking nextBooking = futureBookings.get(0);
                itemDto.setNextBooking(mapToBookingShortDto(nextBooking));
            }
        }

        List<CommentDto> commentDtos = commentRepository.findByItemId(itemId).stream().map(this::mapToCommentDto).collect(Collectors.toList());
        itemDto.setComments(commentDtos);

        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        List<Item> items = itemRepository.findByOwnerId(owner.getId());

        return items.stream().map(item -> {
            ItemDto itemDto = mapToItemDto(item);

            Pageable limitOne = PageRequest.of(0, 1, Sort.by("start").descending());
            Pageable limitOneAsc = PageRequest.of(0, 1, Sort.by("start").ascending());

            List<Booking> pastBookings = bookingRepository.findLastBookingsByItem(item.getId(), List.of(BookingStatus.APPROVED), LocalDateTime.now(), limitOne).getContent();

            if (!pastBookings.isEmpty()) {
                Booking lastBooking = pastBookings.get(0);
                itemDto.setLastBooking(mapToBookingShortDto(lastBooking));
            }

            List<Booking> futureBookings = bookingRepository.findNextBookingsByItem(item.getId(), List.of(BookingStatus.APPROVED), LocalDateTime.now(), limitOneAsc).getContent();

            if (!futureBookings.isEmpty()) {
                Booking nextBooking = futureBookings.get(0);
                itemDto.setNextBooking(mapToBookingShortDto(nextBooking));
            }

            List<CommentDto> commentDtos = commentRepository.findByItemId(item.getId()).stream().map(this::mapToCommentDto).collect(Collectors.toList());
            itemDto.setComments(commentDtos);

            return itemDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }
        List<Item> items = itemRepository.searchByText(text);
        return items.stream().map(this::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long itemId, Long userId, String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment text cannot be blank");
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

        User author = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (item.getOwner().getId().equals(userId)) {
            throw new SecurityException("Owner cannot leave a comment on their own item.");
        }

        List<Booking> approvedPastBookings = bookingRepository.findByItemAndBookerAndStatusInAndEndIsBefore(item, userId, // bookerId
                List.of(BookingStatus.APPROVED), LocalDateTime.now() // now
        );

        if (approvedPastBookings.isEmpty()) {
            throw new IllegalStateException("User has not booked this item before or has no approved past bookings.");
        }

        Comment comment = new Comment(text, item, author, LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return mapToCommentDto(savedComment);
    }

    private ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwnerId(item.getOwner().getId());
        itemDto.setRequestId(item.getRequestId());
        return itemDto;
    }

    private BookingShortDto mapToBookingShortDto(Booking booking) {
        BookingShortDto dto = new BookingShortDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setBookerId(booking.getBooker().getId());
        dto.setStatus(booking.getStatus());
        return dto;
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