package app.networking.dto;

import app.model.User;

public class DtoUtils {

    public static User getFromDto(UserDto userDto){
        User user = new User();
        user.setId(Long.parseLong(userDto.getId()));
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public static UserDto getDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId().toString());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

}
