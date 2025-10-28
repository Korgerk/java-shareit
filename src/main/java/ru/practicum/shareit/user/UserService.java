package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.expectation.ConflictException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User create(User user) {
        validate(user);
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ConflictException("Email уже используется");
        }
        user.setId(idGenerator.getAndIncrement());
        users.put(user.getId(), user);
        return user;
    }

    public User update(Long id, User user) {
        if (!users.containsKey(id)) {
            throw new RuntimeException(String.format("Пользователь с ID %d не найден", id));
        }
        User existing = users.get(id);

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            if (!existing.getEmail().equals(user.getEmail()) && users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                throw new ConflictException("Email уже используется");
            }
            existing.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            existing.setName(user.getName());
        }
        return existing;
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User getById(Long id) {
        return users.get(id);
    }

    public void deleteById(Long id) {
        users.remove(id);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Некорректный email: " + user.getEmail());
        }
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}