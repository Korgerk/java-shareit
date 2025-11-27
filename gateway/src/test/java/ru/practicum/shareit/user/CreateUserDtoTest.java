package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.CreateUserDto;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CreateUserDtoTest {
    private final JacksonTester<CreateUserDto> json;

    @Test
    void testCreateUserDto() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("Hans", "hans@email.com");

        JsonContent<CreateUserDto> data = json.write(createUserDto);

        assertThat(data).extractingJsonPathStringValue("$.name").isEqualTo("Hans");
        assertThat(data).extractingJsonPathStringValue("$.email").isEqualTo("hans@email.com");
    }

}