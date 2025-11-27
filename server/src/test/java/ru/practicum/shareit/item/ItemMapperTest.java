package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ItemMapperTest {
    @Autowired
    private ItemMapper itemMapper;

    @Test
    void mapToItem_WithoutRequest_ShouldMapCorrectly() {
        CreateItemDto dto = new CreateItemDto("drill", "1000W", true, null);

        Item item = itemMapper.mapToItem(dto, null);

        assertNotNull(item);
        assertEquals("drill", item.getName());
        assertEquals("1000W", item.getDescription());
        assertTrue(item.isAvailable());
        assertEquals(null, item.getRequest());
    }
}