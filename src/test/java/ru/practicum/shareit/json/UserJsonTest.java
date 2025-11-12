package ru.practicum.shareit.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@ActiveProfiles("test")
class UserJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void userSerializationDeserialization() throws Exception {
        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setName("Test User");
        originalUser.setEmail("test@example.com");

        String json = objectMapper.writeValueAsString(originalUser);

        User deserializedUser = objectMapper.readValue(json, User.class);

        assertEquals(originalUser.getId(), deserializedUser.getId());
        assertEquals(originalUser.getName(), deserializedUser.getName());
        assertEquals(originalUser.getEmail(), deserializedUser.getEmail());
    }
}