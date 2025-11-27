package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserClient userClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createUser_ValidData_ShouldReturnCreated() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("Hans Zimmer", "hans@example.com");

        when(userClient.createUser(any(CreateUserDto.class)))
                .thenReturn(ResponseEntity.created(URI.create("/users")).build());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createdUser_BlankName_ShouldReturnBadRequest() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("", "hans@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createdUser_InvalidEmail_ShouldReturnBadRequest() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("Hans Zimmer", "hansexample.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_ShouldReturnOk() throws Exception {
        when(userClient.getUser(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ValidData_ShouldReturnOk() throws Exception {
        UpdateUserDto updateDto = new UpdateUserDto("New Name", "new_email@example.com");

        when(userClient.updateUser(anyLong(), any(UpdateUserDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_InvalidEmail_ShouldReturnOk() throws Exception {
        UpdateUserDto updateDto = new UpdateUserDto("New Name", "new_email_example.com");

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception {
        when(userClient.deleteUser(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}