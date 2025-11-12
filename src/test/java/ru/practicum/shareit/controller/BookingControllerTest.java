package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@ActiveProfiles("test")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBooking_ShouldReturnBookingDto() throws Exception {
        BookingDto inputDto = new BookingDto();
        inputDto.setItemId(1L);
        inputDto.setStart(LocalDateTime.now().plusHours(1));
        inputDto.setEnd(LocalDateTime.now().plusHours(2));

        BookingDto outputDto = new BookingDto();
        outputDto.setId(1L);
        outputDto.setItemId(1L);
        outputDto.setStart(inputDto.getStart());
        outputDto.setEnd(inputDto.getEnd());

        when(bookingService.create(any(BookingDto.class), any(Long.class))).thenReturn(outputDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.itemId").value(1L));

        verify(bookingService, times(1)).create(any(BookingDto.class), eq(1L));
    }

    @Test
    void getAllByBooker_ShouldReturnListOfBookingDtos() throws Exception {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(1L);

        when(bookingService.getAllByBooker(eq(userId), any(String.class), any(Integer.class), any(Integer.class))).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(bookingService, times(1)).getAllByBooker(eq(userId), any(String.class), any(Integer.class), any(Integer.class));
    }
}