package ru.practicum.shareit.user;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }
        User user = new User(userDto.getName(), userDto.getEmail());
        User savedUser = userRepository.save(user);
        return mapToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        // Проверяем, что пользователь, которого обновляем, существует
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (userDto.getEmail() != null) {
            // Проверка уникальности email при обновлении (если он меняется)
            if (!userDto.getEmail().equals(existingUser.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
            }
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        User updatedUser = userRepository.save(existingUser);
        return mapToUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}