package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.item.ItemService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemService itemService;

    public ResponseEntity<Object> create(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userService.getById(userId).getBody());
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);
        return ResponseEntity.ok(ItemRequestMapper.toItemRequestDto(savedItemRequest));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllByRequestor(Long userId) {
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        List<ItemRequestDto> requestDtos = requests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllRequests(Long userId, Integer from, Integer size) {
        // Получить все запросы, кроме текущего пользователя
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId, from, size);
        List<ItemRequestDto> requestDtos = requests.stream()
                .map(request -> {
                    ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(request);
                    // Заполнить items
                    List<ru.practicum.shareit.item.dto.ItemDto> items = itemService.findByRequestId(request.getId()).getBody();
                    dto.setItems(items);
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getById(Long userId, Long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

        ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(request);
        // Заполнить items
        List<ru.practicum.shareit.item.dto.ItemDto> items = itemService.findByRequestId(requestId).getBody();
        requestDto.setItems(items);
        return ResponseEntity.ok(requestDto);
    }
}