package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmailIgnoreCase_RepositoryMethod_ShouldReturnTrue() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        entityManager.persistAndFlush(user);

        boolean exists = userRepository.existsByEmailIgnoreCase("TEST@EXAMPLE.COM");

        assertTrue(exists);
    }
}