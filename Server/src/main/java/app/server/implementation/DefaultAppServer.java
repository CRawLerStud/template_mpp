package app.server.implementation;

import app.model.Game;
import app.model.SecretWord;
import app.model.User;
import app.persistance.GameRepository;
import app.persistance.SecretWordRepository;
import app.persistance.UserRepository;
import app.persistance.utils.RepositoryException;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultAppServer implements AppServices {

    private UserRepository userRepository;
    private GameRepository gameRepository;
    private SecretWordRepository secretWordRepository;

    private Map<Long, AppObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    private Map<Long, String> playersWordsReady;
    private Game game = null;
    private Map<Long, String> playersCurrentWords;


    public DefaultAppServer(UserRepository userRepository, GameRepository gameRepository,
                            SecretWordRepository secretWordRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.secretWordRepository = secretWordRepository;
        loggedClients = new ConcurrentHashMap<>();
        playersWordsReady = new ConcurrentHashMap<>();
        playersCurrentWords = new ConcurrentHashMap<>();
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
    public synchronized void logout(Long playerID) throws AppException {
        if(!loggedClients.containsKey(playerID)){
            throw new AppException("User is not logged in!");
        }
        loggedClients.remove(playerID);
    }

    @Override
    public synchronized Integer setUserReady(Long userID, String word) throws AppException {
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

    @Override
    public synchronized Game getGameInstanceForUser(Long userID) throws AppException {
        if(null == game){
            try {Game newGame = new Game();
                Long gameID = gameRepository.save(newGame);
                newGame.setId(gameID);
                this.game = newGame;
            }
            catch(RepositoryException ignored){}
        }

        String baseWord = playersWordsReady.get(userID);

        try {
            SecretWord secretWord = new SecretWord();
            secretWord.setWord(baseWord);
            secretWord.setGame(game);

            User user = userRepository.get(userID);
            secretWord.setUser(user);

            secretWordRepository.save(secretWord);
        }
        catch(RepositoryException ignored){}

        String currWord = "_".repeat(baseWord.length());
        playersCurrentWords.put(userID, currWord);

        return this.game;
    }

    @Override
    public synchronized List<SecretWord> getInformationForUserAndGame(Long userID, Long gameID) throws AppException {
        try {
            List<SecretWord> words = secretWordRepository.getSecretWordsForUser(userID, gameID);
            words.stream().map(word -> {
                word.setWord(playersCurrentWords.get(word.getUser().getId()));
                return word;
            }).toList();
            return words;
        }
        catch(RepositoryException ex){
            return new ArrayList<>();
        }
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
