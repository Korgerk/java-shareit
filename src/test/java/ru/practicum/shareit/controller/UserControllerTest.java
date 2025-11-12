package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_ShouldReturnUser() throws Exception {
        User inputUser = new User();
        inputUser.setName("Test User");
        inputUser.setEmail("test@example.com");

        User outputUser = new User();
        outputUser.setId(1L);
        outputUser.setName("Test User");
        outputUser.setEmail("test@example.com");

        when(userService.create(any(User.class))).thenReturn(outputUser);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputUser))).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Test User")).andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void getById_ShouldReturnUser() throws Exception {
        Long userId = 1L;
        User outputUser = new User();
        outputUser.setId(userId);
        outputUser.setName("Test User");
        outputUser.setEmail("test@example.com");

        when(userService.getById(eq(userId))).thenReturn(outputUser);

        mockMvc.perform(get("/users/{id}", userId)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(userId)).andExpect(jsonPath("$.name").value("Test User")).andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getById(eq(userId));
    }

    @Test
    void getAll_ShouldReturnListOfUsers() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id").value(1L)).andExpect(jsonPath("$[0].name").value("Test User")).andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService, times(1)).getAll();
    }
}