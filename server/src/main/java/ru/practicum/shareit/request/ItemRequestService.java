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
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
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
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Описание запроса не может быть пустым");
        }
        userService.getById(userId);

        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequestor(userService.getById(userId));
        request.setCreated(LocalDateTime.now());

        request = itemRequestRepository.save(request);

        ItemRequestDto resultDto = new ItemRequestDto();
        resultDto.setId(request.getId());
        resultDto.setDescription(request.getDescription());
        resultDto.setCreated(request.getCreated());
        return resultDto;
    }

    @Transactional(readOnly = true)
    public List<ItemRequestWithItemsDto> getAllByRequestor(Long userId) {
        userService.getById(userId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId);

        return requests.stream().map(this::toItemRequestWithItemsDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemRequestWithItemsDto> getAllRequests(Long userId, int from, int size) {
        userService.getById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from / size, size, sort);

        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdNotOrderByCreatedDesc(userId, pageable);

        return requests.stream().map(this::toItemRequestWithItemsDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemRequestWithItemsDto getById(Long requestId, Long userId) {
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException(String.format("Запрос с ID %d не найден", requestId)));

        return toItemRequestWithItemsDto(request);
    }

    private ItemRequestWithItemsDto toItemRequestWithItemsDto(ItemRequest request) {
        ItemRequestWithItemsDto dto = new ItemRequestWithItemsDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());

        List<Item> itemsForRequest = itemRepository.findByRequest_Id(request.getId());
        List<ItemRequestWithItemsDto.ItemInRequestDto> itemsDto = itemsForRequest.stream().map(item -> {
            ItemRequestWithItemsDto.ItemInRequestDto itemDto = new ItemRequestWithItemsDto.ItemInRequestDto();
            itemDto.setId(item.getId());
            itemDto.setName(item.getName());
            itemDto.setDescription(item.getDescription());
            itemDto.setOwnerId(item.getOwner().getId());
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemsDto);
        return dto;
    }
}