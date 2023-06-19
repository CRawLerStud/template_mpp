package app.server.implementation;

import app.model.User;
import app.persistance.UserRepository;
import app.persistance.utils.RepositoryException;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultAppServer implements AppServices {

    private UserRepository userRepository;

    private Map<Long, AppObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    private Map<Long, String> playersWordsReady;


    public DefaultAppServer(UserRepository userRepository) {
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
        playersWordsReady = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized User login(String username, String password, AppObserver client) throws AppException {
        User user;
        try{
            user = userRepository.checkCredentials(username, password);

            if(loggedClients.containsKey(user.getId())){
                throw new AppException("User already logged in!");
            }

            loggedClients.put(user.getId(), client);
        }
        catch(RepositoryException ex){
            throw new AppException("Authentication failed: " + ex.getMessage());
        }
        return user;
    }

    @Override
    public void logout(Long playerID) throws AppException {
        if(!loggedClients.containsKey(playerID)){
            throw new AppException("User is not logged in!");
        }
        loggedClients.remove(playerID);
    }

    @Override
    public Integer setUserReady(Long userID, String word) throws AppException {
        if(!loggedClients.containsKey(userID)){
            throw new AppException("User is not logged in!");
        }
        if(playersWordsReady.containsKey(userID)){
            throw new AppException("User is already ready!");
        }
        playersWordsReady.put(userID, word);
        notifyPlayerReady(playersWordsReady.size());
        return playersWordsReady.size();
    }

    private void notifyPlayerReady(int size) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        executor.execute(() -> {
            for(AppObserver observer : loggedClients.values()){
                if(observer != null){
                    try {
                        observer.notifyUserReady(size);
                    }
                    catch(AppException ignored){}
                }
            }
        });
    }
}
