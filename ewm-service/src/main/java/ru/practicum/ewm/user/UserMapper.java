package ru.practicum.ewm.user;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static List<UserDto> toListUserDto(List<User> usersList) {
        List<UserDto> itemDtos = new ArrayList<>();
        for (User user : usersList) {
            itemDtos.add(toUserDto(user));
        }
        return itemDtos;
    }

}
