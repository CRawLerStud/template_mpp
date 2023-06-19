package app.services;

import app.model.User;

public interface AppServices{
    User login(String username, String password, AppObserver client) throws AppException;
    void logout(Long playerID) throws AppException;
    Integer setUserReady(Long userID, String word) throws AppException;
}
