package app.services;

import app.model.Game;

public interface AppObserver {
    void notifyFinishedGame(Game game) throws AppException;
}
