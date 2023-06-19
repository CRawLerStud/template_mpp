package app.networking.dto;

import app.model.Game;
import app.model.SecretWord;
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
        return new GameDto(game.getId());
    }

    public static Game getFromDto(GameDto gameDto){
        Game game = new Game();
        game.setId(gameDto.getId());
        return game;
    }

    public static SecretWord getFromDto(SecretWordDto secretWordDto){
        SecretWord secretWord = new SecretWord();
        secretWord.setId(secretWordDto.getId());
        secretWord.setWord(secretWordDto.getWord());

        User user = new User();
        user.setId(secretWordDto.getUserID());
        user.setUsername(secretWordDto.getUsername());
        user.setPassword(secretWordDto.getPassword());

        Game game = new Game();
        game.setId(secretWordDto.getGameID());

        secretWord.setGame(game);
        secretWord.setUser(user);

        return secretWord;
    }

    public static SecretWordDto getDto(SecretWord secretWord){
        SecretWordDto secretWordDto = new SecretWordDto();
        secretWordDto.setId(secretWord.getId());
        secretWordDto.setWord(secretWord.getWord());

        secretWordDto.setUserID(secretWord.getUser().getId());
        secretWordDto.setUsername(secretWord.getUser().getUsername());
        secretWordDto.setPassword(secretWord.getUser().getPassword());

        secretWordDto.setGameID(secretWord.getGame().getId());

        return secretWordDto;
    }


}
