package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void addItem_WithoutRequest_ShouldSave() {
        User user = User.builder().id(1L).name("Vasya").email("vas@ya.ru").build();
        CreateItemDto createDto = new CreateItemDto("pen", "blue pen", true, null);
        Item item = Item.builder().id(1L).owner(user).name("pen").description("blue pen").available(true).build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(itemMapper.mapToItem(createDto, null))
                .thenReturn(item);
        when(itemRepository.save(any()))
                .thenReturn(item);

        ItemDto result = itemService.addItem(1L, createDto);

        assertEquals(item.getName(), createDto.getName());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void addItem_NotExistingUser_ShouldThrowException() {
        Long userId = 13L;
        CreateItemDto createDto = new CreateItemDto("pen", "blue pen", true, null);

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.addItem(userId, createDto));

        verify(userRepository).findById(userId);
        verifyNoInteractions(itemRepository);
        verifyNoInteractions(itemMapper);
    }

}