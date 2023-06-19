package app.services;

import app.model.Game;
import app.model.SecretWord;
import app.model.User;

import java.util.List;

public interface AppServices{
    User login(String username, String password, AppObserver client) throws AppException;
    void logout(Long playerID) throws AppException;
    Integer setUserReady(Long userID, String word) throws AppException;
    Game getGameInstanceForUser(Long userID) throws AppException;
    List<SecretWord> getInformationForUserAndGame(Long userID, Long gameID) throws AppException;
}
