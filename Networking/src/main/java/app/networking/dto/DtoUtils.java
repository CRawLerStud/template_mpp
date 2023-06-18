package app.networking.dto;

import app.model.Configuration;
import app.model.Game;
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

    public static GameDto getDto(Game game){
        GameDto gameDto = new GameDto();
        gameDto.setId(game.getId().toString());

        gameDto.setConfigurationId(game.getConfiguration().getId().toString());
        gameDto.setPosition(game.getConfiguration().getPosition().toString());
        gameDto.setHint(game.getConfiguration().getHint());

        gameDto.setUserId(game.getUser().getId().toString());
        gameDto.setUsername(game.getUser().getUsername());
        gameDto.setPassword(game.getUser().getPassword());

        gameDto.setWon(game.getWon());
        gameDto.setFinished(game.getFinished());
        gameDto.setDate(game.getDate());
        gameDto.setPoints(game.getPoints());

        return gameDto;
    }

    public static Game getFromDto(GameDto gameDto){
        Game game = new Game();
        game.setId(Long.parseLong(gameDto.getId()));

        Configuration configuration = new Configuration();
        configuration.setId(Long.parseLong(gameDto.getConfigurationId()));
        configuration.setPosition(Integer.parseInt(gameDto.getPosition()));
        configuration.setHint(gameDto.getHint());

        User user = new User();
        user.setId(Long.parseLong(gameDto.getUserId()));
        user.setUsername(gameDto.getUsername());
        user.setPassword(gameDto.getPassword());

        game.setConfiguration(configuration);
        game.setUser(user);

        game.setWon(gameDto.getWon());
        game.setFinished(gameDto.getFinished());
        game.setDate(gameDto.getDate());
        game.setPoints(gameDto.getPoints());

        return game;
    }


}
