package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;


public interface ItemRequestService {

    ItemRequestDto createRequest(long userId, CreateItemRequestDto createRequest);

    ItemRequestDto getItemRequest(long requestId);

    List<ItemRequestDto> getUserRequests(long userId);

    List<ItemRequestDto> getOtherUsersRequests(long userId);
}