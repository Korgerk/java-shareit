package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItem_ValidData_ShouldReturnCreatedItem() throws Exception {
        CreateItemDto createItemDto = new CreateItemDto("pencil", "blue pencil", true, null);
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("pencil")
                .description("blue pencil")
                .available(true)
                .build();

        when(itemService.addItem(anyLong(), any(CreateItemDto.class)))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("pencil")))
                .andExpect(jsonPath("$.description", is("blue pencil")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void search_ShouldReturnItems() throws Exception {
        ItemDto itemDto1 = ItemDto.builder()
                .id(1L)
                .name("pencil")
                .description("blue pencil")
                .available(true)
                .build();

        ItemDto itemDto2 = ItemDto.builder()
                .id(2L)
                .name("pen")
                .description("office pen")
                .available(true)
                .build();

        ItemDto itemDto3 = ItemDto.builder()
                .id(3L)
                .name("peen")
                .description("peen")
                .available(true)
                .build();

        when(itemService.searchItems(anyString()))
                .thenReturn(List.of(itemDto1, itemDto2));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "pen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("pencil")))
                .andExpect(jsonPath("$[1].name", is("pen")));
    }

    @Test
    void addComment_ShouldReturnSavedComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("very usefull")
                .authorName("Vasya")
                .build();

        when(itemService.addComment(anyLong(), anyLong(), any(CreateCommentDto.class)))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateCommentDto("very usefull"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("very usefull")))
                .andExpect(jsonPath("$.authorName", is("Vasya")));
    }
}