package ru.practicum.shareit.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@ActiveProfiles("test")
class ItemDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itemDtoSerializationDeserialization() throws Exception {
        ItemDto originalDto = new ItemDto();
        originalDto.setId(1L);
        originalDto.setName("Test Item");
        originalDto.setDescription("Test Description");
        originalDto.setAvailable(true);

        String json = objectMapper.writeValueAsString(originalDto);

        ItemDto deserializedDto = objectMapper.readValue(json, ItemDto.class);

        assertEquals(originalDto.getId(), deserializedDto.getId());
        assertEquals(originalDto.getName(), deserializedDto.getName());
        assertEquals(originalDto.getDescription(), deserializedDto.getDescription());
        assertEquals(originalDto.getAvailable(), deserializedDto.getAvailable());
    }
}