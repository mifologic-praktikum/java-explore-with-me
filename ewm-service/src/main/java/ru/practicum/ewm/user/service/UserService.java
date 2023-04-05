package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers(List<Long> usersIds, int from, int size);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long userId);

}
