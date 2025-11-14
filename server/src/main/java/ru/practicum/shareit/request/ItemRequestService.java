package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.expectation.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestService {

    private static final String ERROR_MESSAGE_REQUEST_NOT_FOUND = "Запрос с ID %d не найден";
    private static final String ERROR_MESSAGE_USER_NOT_FOUND = "Пользователь с ID %d не найден";

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public ItemRequestDto create(ItemRequestDto dto, Long userId) {
        userService.getById(userId);

        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setCreated(LocalDateTime.now());
        request.setRequestor(userService.getById(userId));

        ItemRequest savedRequest = itemRequestRepository.save(request);

        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(savedRequest.getId());
        responseDto.setDescription(savedRequest.getDescription());
        responseDto.setCreated(savedRequest.getCreated());
        responseDto.setItems(List.of());
        return responseDto;
    }

    @Transactional(readOnly = true)
    public ItemRequestDto getById(Long requestId, Long userId) {
        userService.getById(userId);

        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_REQUEST_NOT_FOUND, requestId)));

        List<Item> itemsForRequest = itemRepository.findByRequestId(requestId);

        List<ItemDto> itemDtos = itemsForRequest.stream()
                .map(item -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setName(item.getName());
                    return itemDto;
                })
                .collect(Collectors.toList());

        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(request.getId());
        responseDto.setDescription(request.getDescription());
        responseDto.setCreated(request.getCreated());
        responseDto.setItems(itemDtos);

        return responseDto;
    }

    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllByRequestor(Long userId) {
        userService.getById(userId);

        List<ItemRequest> requests = itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId);

        Map<Long, List<Item>> itemsByRequestId = itemRepository.findByRequestIdIn(
                        requests.stream().map(ItemRequest::getId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRequestId()));

        return requests.stream()
                .map(request -> {
                    ItemRequestDto dto = new ItemRequestDto();
                    dto.setId(request.getId());
                    dto.setDescription(request.getDescription());
                    dto.setCreated(request.getCreated());

                    List<ItemDto> itemDtos = itemsByRequestId.getOrDefault(request.getId(), List.of())
                            .stream()
                            .map(item -> {
                                ItemDto itemDto = new ItemDto();
                                itemDto.setId(item.getId());
                                itemDto.setName(item.getName());
                                return itemDto;
                            })
                            .collect(Collectors.toList());

                    dto.setItems(itemDtos);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAll(Long userId, int from, int size) {
        userService.getById(userId);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));

        var page = itemRequestRepository.findAllByRequestor_IdNot(userId, pageable);

        List<ItemRequest> requests = page.getContent();

        List<Long> requestIds = requests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        Map<Long, List<Item>> itemsByRequestId = itemRepository.findByRequestIdIn(requestIds)
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRequestId()));

        return requests.stream()
                .map(request -> {
                    ItemRequestDto dto = new ItemRequestDto();
                    dto.setId(request.getId());
                    dto.setDescription(request.getDescription());
                    dto.setCreated(request.getCreated());

                    List<ItemDto> itemDtos = itemsByRequestId.getOrDefault(request.getId(), List.of())
                            .stream()
                            .map(item -> {
                                ItemDto itemDto = new ItemDto();
                                itemDto.setId(item.getId());
                                itemDto.setName(item.getName());
                                return itemDto;
                            })
                            .collect(Collectors.toList());

                    dto.setItems(itemDtos);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}