package app.server.implementation;

import app.model.User;
import app.persistance.UserRepository;
import app.persistance.utils.RepositoryException;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAppServer implements AppServices {

    private UserRepository userRepository;

    private Map<Long, AppObserver> loggedClients;
    private final int defaultThreadsNo = 5;


    public DefaultAppServer(UserRepository userRepository) {
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized User login(String username, String password, AppObserver client) throws AppException {
        User user = null;
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
}
