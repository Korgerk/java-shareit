package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.expectation.ConflictException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User create(User user) {
        validate(user);
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ConflictException("Email уже используется");
        }
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        User existing = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Пользователь с ID %d не найден", id)));

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            if (!existing.getEmail().equals(user.getEmail()) && userRepository.existsByEmailIgnoreCase(user.getEmail())) {
                throw new ConflictException("Email уже используется");
            }
            existing.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            existing.setName(user.getName());
        }
        return existing;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
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

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}