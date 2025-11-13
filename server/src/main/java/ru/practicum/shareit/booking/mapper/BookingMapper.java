package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.mapper.DateMapper;


@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface BookingMapper {

    Booking mapToBooking(CreateBookingDto createBookingDto);

    @Mapping(source = "item.id", target = "item.id")
    @Mapping(source = "item.name", target = "item.name")
    @Mapping(source = "user.id", target = "booker.id")
    @Mapping(source = "user.name", target = "booker.name")
    BookingDto mapToBookingDto(Booking booking);

}