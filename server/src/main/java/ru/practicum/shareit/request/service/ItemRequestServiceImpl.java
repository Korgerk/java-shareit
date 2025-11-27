package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDto createRequest(long userId, CreateItemRequestDto createRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user-owner not found"));

        return itemRequestMapper.mapToItemRequestDto(
                itemRequestRepository.save(ItemRequest.builder()
                        .description(createRequest.getDescription())
                        .author(user)
                        .created(Instant.now())
                        .build())
        );
    }

    @Override
    public ItemRequestDto getItemRequest(long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("item request not found"));

        return itemRequestMapper.mapToItemRequestDto(request);
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        return itemRequestRepository.findByAuthorIdOrderByCreatedDesc(userId)
                .stream()
                .map(itemRequestMapper::mapToItemRequestDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> getOtherUsersRequests(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        return itemRequestRepository.findByAuthorIdNotOrderByCreatedDesc(userId)
                .stream()
                .map(itemRequestMapper::mapToItemRequestDto)
                .toList();
    }
}