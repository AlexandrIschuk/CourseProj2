package ru.ssau.carshwebcourse.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.User;

@Component
public class UserMappingUtils {
    public User mapToUserEntity(UserDto dto){
        User entity = new User();
        entity.setUserId(dto.getUserId());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setFirstname(dto.getFirstname());
        entity.setLastname(dto.getLastname());
        entity.setDrvLicenseNumber(dto.getDrvLicenseNumber());
        entity.setRegisterDate(dto.getRegisterDate());
        entity.setRole(dto.getRole());
        return entity;
    }
    public UserDto mapToUserDto(User entity){
        UserDto userDto = new UserDto();
        userDto.setUserId(entity.getUserId());
        userDto.setEmail(entity.getEmail());
        userDto.setPassword(entity.getPassword());
        userDto.setPhoneNumber(entity.getPhoneNumber());
        userDto.setFirstname(entity.getFirstname());
        userDto.setLastname(entity.getLastname());
        userDto.setDrvLicenseNumber(entity.getDrvLicenseNumber());
        userDto.setRegisterDate(entity.getRegisterDate());
        userDto.setRole(entity.getRole());
        return userDto;
    }
}
