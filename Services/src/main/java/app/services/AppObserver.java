package app.services;

public interface AppObserver {
    void notifyUserReady(Integer noOfPlayersReady) throws AppException;
}
