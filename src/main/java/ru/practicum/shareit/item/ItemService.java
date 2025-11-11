package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.expectation.AccessDeniedException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemDto create(ItemDto dto, Long userId) {
        validate(dto);
        User owner = userService.getById(userId);

        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);
        item = itemRepository.save(item);
        return toItemDto(item);
    }

    public ItemDto update(Long itemId, ItemDto dto, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException(String.format("Вещь с ID %d не найдена", itemId)));

        if (!item.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException(String.format("Пользователь %d не является владельцем вещи %d", userId, itemId));
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        item = itemRepository.save(item);
        return toItemDto(item);
    }

    @Transactional(readOnly = true)
    public ItemDto getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Вещь не найдена"));
        return toItemDtoWithDetails(item);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getOwnerItems(Long userId) {
        userService.getById(userId);
        List<Item> items = itemRepository.findByOwner_Id(userId, PageRequest.of(0, 1000)).getContent();
        return items.stream().map(this::toItemDtoWithDetails).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<Item> items = itemRepository.search(text);
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
    }

    public CommentDto addComment(Long itemId, Long userId, Comment commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException(String.format("Вещь с ID %d не найдена", itemId)));

        User author = userService.getById(userId);

        List<Booking> pastBookings = bookingRepository.findPastBookingsByBooker(userId, LocalDateTime.now());
        boolean hasBooked = pastBookings.stream().anyMatch(b -> b.getItem().getId().equals(itemId));

        if (!hasBooked) {
            throw new IllegalArgumentException("Пользователь не может оставить комментарий, так как не брал вещь в аренду.");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        comment = commentRepository.save(comment);
        return toCommentDto(comment);
    }

    private ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    private ItemDto toItemDtoWithDetails(Item item) {
        ItemDto dto = toItemDto(item);
        List<CommentDto> comments = commentRepository.findByItem_IdOrderByCreatedDesc(item.getId()).stream().map(this::toCommentDto).collect(Collectors.toList());
        dto.setComments(comments);

        List<Booking> lastApprovedPastBookings = bookingRepository.findApprovedPastBookingsForItem(item.getId(), LocalDateTime.now());
        if (!lastApprovedPastBookings.isEmpty()) {
            Booking lastBooking = lastApprovedPastBookings.get(0);
            BookingShortDto lastDto = new BookingShortDto();
            lastDto.setId(lastBooking.getId());
            lastDto.setStart(lastBooking.getStart());
            lastDto.setEnd(lastBooking.getEnd());
            lastDto.setBookerId(lastBooking.getBooker().getId());
            lastDto.setStatus(lastBooking.getStatus().toString());
            dto.setLastBooking(lastDto);
        } else {
            dto.setLastBooking(null);
        }

        List<Booking> nextApprovedFutureBookings = bookingRepository.findApprovedFutureBookingsForItem(item.getId(), LocalDateTime.now());
        if (!nextApprovedFutureBookings.isEmpty()) {
            Booking nextBooking = nextApprovedFutureBookings.get(0);
            BookingShortDto nextDto = new BookingShortDto();
            nextDto.setId(nextBooking.getId());
            nextDto.setStart(nextBooking.getStart());
            nextDto.setEnd(nextBooking.getEnd());
            nextDto.setBookerId(nextBooking.getBooker().getId());
            nextDto.setStatus(nextBooking.getStatus().toString());
            dto.setNextBooking(nextDto);
        } else {
            dto.setNextBooking(null);
        }
        return dto;
    }

    private CommentDto toCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }

    private void validate(ItemDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Описание не может быть пустым");
        }
        if (dto.getAvailable() == null) {
            throw new IllegalArgumentException("Поле 'available' обязательно");
        }
    }
}