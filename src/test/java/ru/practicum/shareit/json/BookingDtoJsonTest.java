package ru.practicum.shareit.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@ActiveProfiles("test")
class BookingDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void bookingDtoSerializationDeserialization() throws Exception {
        BookingDto originalDto = new BookingDto();
        originalDto.setId(1L);
        originalDto.setStart(LocalDateTime.now().plusHours(1));
        originalDto.setEnd(LocalDateTime.now().plusHours(2));
        originalDto.setItemId(1L);
        originalDto.setStatus(BookingStatus.WAITING);

        String json = objectMapper.writeValueAsString(originalDto);

        BookingDto deserializedDto = objectMapper.readValue(json, BookingDto.class);

        assertEquals(originalDto.getId(), deserializedDto.getId());
        assertEquals(originalDto.getStart(), deserializedDto.getStart());
        assertEquals(originalDto.getEnd(), deserializedDto.getEnd());
        assertEquals(originalDto.getItemId(), deserializedDto.getItemId());
        assertEquals(originalDto.getStatus(), deserializedDto.getStatus());
    }
}