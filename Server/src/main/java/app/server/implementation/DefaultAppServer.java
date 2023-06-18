package app.server.implementation;

import app.model.Configuration;
import app.model.Game;
import app.model.Move;
import app.model.User;
import app.persistance.ConfigurationRepository;
import app.persistance.GameRepository;
import app.persistance.MoveRepository;
import app.persistance.UserRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.net.ServerSocket;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultAppServer implements AppServices {

    private UserRepository userRepository;
    private ConfigurationRepository configurationRepository;
    private GameRepository gameRepository;
    private MoveRepository moveRepository;

    private Map<Long, AppObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    private Map<Long, Game> playersInGame;
    private Map<Game, Integer> gameAndRounds;


    public DefaultAppServer(UserRepository userRepository, ConfigurationRepository configurationRepository,
                            GameRepository gameRepository, MoveRepository moveRepository) {
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
        loggedClients = new ConcurrentHashMap<>();
        playersInGame = new ConcurrentHashMap<>();
        gameAndRounds = new ConcurrentHashMap<>();
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
    public synchronized void logout(Long userID) throws AppException {
        if(!loggedClients.containsKey(userID)){
            throw new AppException("User is not logged in!");
        }
        if (playersInGame.containsKey(userID)) {
            Game game = playersInGame.get(userID);
            gameAndRounds.remove(game);
            playersInGame.remove(userID);
        }
        loggedClients.remove(userID);
    }

    @Override
    public synchronized Game startGameForUser(Long userID) throws AppException{
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
            gameAndRounds.put(game, 0);

            return game;
        }
        catch(RepositoryException exception){
            throw new AppException(exception.getMessage());
        }
    }

    @Override
    public synchronized Object sendMoveForUser(Long playerID, Integer position) throws AppException {
        try{
            Move move = new Move();
            User user = userRepository.get(playerID);
            Game game = playersInGame.get(user.getId());
            move.setUser(user);
            move.setGame(game);
            move.setPosition(position);
            Long moveId = moveRepository.save(move);
            move.setId(moveId);

            if(move.getPosition().equals(game.getConfiguration().getPosition())){
                //WIN LOGIC
                playersInGame.remove(playerID);
                gameAndRounds.remove(game);

                String hint = game.getConfiguration().getHint();

                game.setFinished(true);
                game.setWon(true);
                game.setPoints(moveRepository.getSizeForGame(game.getId()));
                gameRepository.update(game);

                notifyFinishedGame(game);

                return hint;
            }
            else{
                Double distance = calculateEuclidianDistance(move.getPosition(), game.getConfiguration().getPosition());

                Integer currentRound = gameAndRounds.get(game);
                currentRound += 1;
                gameAndRounds.put(game, currentRound);

                if(currentRound == 4){
                    //LOSE LOGIC
                    playersInGame.remove(playerID);
                    gameAndRounds.remove(game);

                    game.setWon(false);
                    game.setFinished(true);
                    game.setPoints(10);
                    gameRepository.update(game);

                    notifyFinishedGame(game);

                    return currentRound;
                }
                //ANOTHER CHANGE LOGIC
                return distance;
            }
        }
        catch(RepositoryException ex){
            throw new AppException(ex.getMessage());
        }
    }

    @Override
    public List<Game> getGamesForConfiguration(Long configurationID) throws AppException {
        try{
            List<Game> games = gameRepository.getAllGamesForConfiguration(configurationID);
            return games;
        }
        catch(RepositoryException ex){
            throw new AppException(ex.getMessage());
        }
    }

    private void notifyFinishedGame(Game game) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(AppObserver observer : loggedClients.values()){
            if(observer != null){
                executor.execute(() -> {
                    try{
                        System.out.println("Notifying finished game!");
                        observer.notifyFinishedGame(game);
                    }
                    catch(AppException ex){
                        System.out.println("Error while notifying finished game: " + ex.getMessage());
                    }
                });
            }
        }
    }

    private Double calculateEuclidianDistance(Integer position, Integer position1) {
        int x1 = position / 4;
        int y1 = position - x1 * 4;
        int x2 = position1 / 4;
        int y2 = position1 - x2 * 4;
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
