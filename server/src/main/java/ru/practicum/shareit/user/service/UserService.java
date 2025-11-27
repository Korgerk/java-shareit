package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto createUser(CreateUserDto user);

    UserDto getUser(long id);

    UserDto updateUser(long id, UpdateUserDto updateUserInfo);

    void deleteUser(long id);

}