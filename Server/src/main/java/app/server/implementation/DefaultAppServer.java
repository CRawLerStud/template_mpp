package app.server.implementation;

import app.model.Configuration;
import app.model.Game;
import app.model.User;
import app.persistance.ConfigurationRepository;
import app.persistance.GameRepository;
import app.persistance.UserRepository;
import app.persistance.utils.RepositoryException;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAppServer implements AppServices {

    private UserRepository userRepository;
    private ConfigurationRepository configurationRepository;
    private GameRepository gameRepository;

    private Map<Long, AppObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    private Map<Long, Game> playersInGame;


    public DefaultAppServer(UserRepository userRepository, ConfigurationRepository configurationRepository,
                            GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
        this.gameRepository = gameRepository;
        loggedClients = new ConcurrentHashMap<>();
        playersInGame = new ConcurrentHashMap<>();
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
    public void logout(Long userID) throws AppException {
        if(!loggedClients.containsKey(userID)){
            throw new AppException("User is not logged in!");
        }
        playersInGame.remove(userID);
        loggedClients.remove(userID);
    }

    @Override
    public Game startGameForUser(Long userID) throws AppException{
        try {
            Configuration configuration = configurationRepository.getRandomConfiguration();
            User user = userRepository.get(userID);

            Game game = new Game();
            game.setUser(user);
            game.setConfiguration(configuration);
            game.setDate(LocalDate.now());
            game.setFinished(false);
            game.setWon(false);
            Long gameID = gameRepository.save(game);
            game.setId(gameID);

            playersInGame.put(userID, game);

            return game;
        }
        catch(RepositoryException exception){
            throw new AppException(exception.getMessage());
        }
    }
}
