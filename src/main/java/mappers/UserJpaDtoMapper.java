package mappers;

import models.UserJpa;
import dtos.UserDto;

public class UserJpaDtoMapper {

    private UserJpaDtoMapper(){}

    public static UserJpa toUserJpa(UserDto userDto) {
        return new UserJpa(userDto.id(), userDto.email(), userDto.password(), userDto.userType());
    }

    public static UserDto toUserDto(UserJpa userJpa) {
        return new UserDto(userJpa.getId(), userJpa.getEmail(), userJpa.getPassword(), userJpa.getUserType());
    }

}
