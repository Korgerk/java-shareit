package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.mapper.DateMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface ItemRequestMapper {

    ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest);

}