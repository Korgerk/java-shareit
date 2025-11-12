package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public ItemRequestDto create(ItemRequestDto dto, Long userId) {
        validate(dto);
        User requestor = userService.getById(userId);
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        request = itemRequestRepository.save(request);
        return toItemRequestDto(request);
    }

    @Transactional(readOnly = true)
    public List<ItemRequestOutputDto> getAllByRequestor(Long userId) {
        userService.getById(userId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId);
        return requests.stream().map(this::toItemRequestOutputDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemRequestOutputDto> getAllRequests(Long userId, int from, int size) {
        userService.getById(userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdNot(userId, pageable);
        return requests.stream().map(this::toItemRequestOutputDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemRequestOutputDto getById(Long requestId, Long userId) {
        userService.getById(userId); // Проверяем существование пользователя
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Запрос с ID " + requestId + " не найден"));
        return toItemRequestOutputDto(request);
    }

    private ItemRequestDto toItemRequestDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    private ItemRequestOutputDto toItemRequestOutputDto(ItemRequest request) {
        ItemRequestOutputDto dto = new ItemRequestOutputDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        List<Item> itemsForRequest = itemRepository.findByItemRequest_Id(request.getId());
        List<ItemRequestOutputDto.ItemInRequestDto> itemsDto = itemsForRequest.stream().map(item -> {
            ItemRequestOutputDto.ItemInRequestDto itemDto = new ItemRequestOutputDto.ItemInRequestDto();
            itemDto.setId(item.getId());
            itemDto.setName(item.getName());
            itemDto.setDescription(item.getDescription());
            itemDto.setOwnerId(item.getOwner().getId());
            return itemDto;
        }).collect(Collectors.toList());
        dto.setItems(itemsDto);
        return dto;
    }

    private void validate(ItemRequestDto dto) {
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Описание запроса не может быть пустым");
        }
    }
}