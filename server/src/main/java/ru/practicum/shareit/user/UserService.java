package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<Object> create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(UserMapper.toUserDto(savedUser));
    }

    public ResponseEntity<Object> update(Long id, UserUpdateDto userUpdateDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (userUpdateDto.getName() != null) {
            existingUser.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            existingUser.setEmail(userUpdateDto.getEmail());
        }

        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(UserMapper.toUserDto(updatedUser));
    }

    public ResponseEntity<Object> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    public ResponseEntity<Object> getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return ResponseEntity.ok(UserMapper.toUserDto(user));
    }

    public ResponseEntity<Object> delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}