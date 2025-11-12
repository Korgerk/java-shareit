package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_ShouldReturnItemDto() throws Exception {
        ItemDto inputDto = new ItemDto();
        inputDto.setName("Test Item");
        inputDto.setDescription("Test Description");
        inputDto.setAvailable(true);

        ItemDto outputDto = new ItemDto();
        outputDto.setId(1L);
        outputDto.setName("Test Item");
        outputDto.setDescription("Test Description");
        outputDto.setAvailable(true);

        when(itemService.create(any(ItemDto.class), any(Long.class))).thenReturn(outputDto);

        mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1L).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDto))).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Test Item")).andExpect(jsonPath("$.description").value("Test Description")).andExpect(jsonPath("$.available").value(true));

        verify(itemService, times(1)).create(any(ItemDto.class), eq(1L));
    }

    @Test
    void getById_ShouldReturnItemDtoWithDetails() throws Exception {
        Long itemId = 1L;
        Long userId = 2L;
        ItemDto outputDto = new ItemDto();
        outputDto.setId(itemId);
        outputDto.setName("Test Item");
        outputDto.setDescription("Test Description");
        outputDto.setAvailable(true);

        when(itemService.getById(eq(itemId), eq(userId))).thenReturn(outputDto);

        mockMvc.perform(get("/items/{itemId}", itemId).header("X-Sharer-User-Id", userId)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(itemId)).andExpect(jsonPath("$.name").value("Test Item")).andExpect(jsonPath("$.description").value("Test Description"));

        verify(itemService, times(1)).getById(eq(itemId), eq(userId));
    }

    @Test
    void getOwnerItems_ShouldReturnListOfItemDtos() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        when(itemService.getOwnerItems(eq(userId))).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items").header("X-Sharer-User-Id", userId)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id").value(1L)).andExpect(jsonPath("$[0].name").value("Test Item")).andExpect(jsonPath("$[0].description").value("Test Description"));

        verify(itemService, times(1)).getOwnerItems(eq(userId));
    }
}