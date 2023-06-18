package app.services;

import app.model.Game;
import app.model.User;

import java.util.List;

public interface AppServices{
    User login(String username, String password, AppObserver client) throws AppException;
    void logout(Long userID) throws AppException;
    Game startGameForUser(Long userID) throws AppException;
    Object sendMoveForUser(Long playerID, Integer position) throws AppException;
    List<Game> getGamesForConfiguration(Long configurationID) throws AppException;
}
