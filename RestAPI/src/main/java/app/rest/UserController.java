package app.rest;

import app.model.Configuration;
import app.model.Game;
import app.model.Move;
import app.model.User;
import app.persistance.ConfigurationRepository;
import app.persistance.GameRepository;
import app.persistance.UserRepository;
import app.persistance.utils.RepositoryException;
import app.rest.dto.GameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final ConfigurationRepository configurationRepository;
    private final GameRepository gameRepository;

    @Autowired
    public UserController(UserRepository userRepository, ConfigurationRepository configurationRepository,
                          GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
        this.gameRepository = gameRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public User[] getAll(){
        try {
            return userRepository.getAll().toArray(User[]::new);
        }
        catch(RepositoryException ex){
            return new User[0];
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> createConfiguration(@RequestBody Configuration configuration){
        try{
            Long id = configurationRepository.save(configuration);
            return ResponseEntity.ok("Configuration saved successfully: " + id.toString() + " is the new id!");
        }
        catch(RepositoryException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/{playerID}", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllGamesForPlayer(@PathVariable Long playerID){
        try{
            List<Game> games = gameRepository.getAllGamesForPlayer(playerID);
            List<GameDto> gameDtos = new ArrayList<>();
            for(Game game : games){
                GameDto gameDto = new GameDto();
                gameDto.setId(game.getId());
                gameDto.setHint("");
                if(game.getWon()) {
                    gameDto.setHint(game.getConfiguration().getHint());
                }
                gameDto.setNoOfTries(game.getMoves().size());
                List<Move> moves = game.getMoves();
                List<Integer> positions = new ArrayList<>();
                for(Move move : moves){
                    positions.add(move.getPosition());
                }
                gameDto.setPositions(positions);
                gameDtos.add(gameDto);
            }
            return ResponseEntity.ok(gameDtos);
        }
        catch(RepositoryException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
