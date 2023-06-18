package app.rest;

import app.model.Configuration;
import app.model.Game;
import app.model.User;
import app.persistance.ConfigurationRepository;
import app.persistance.GameRepository;
import app.persistance.UserRepository;
import app.persistance.utils.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok(gameRepository.getAllGamesForPlayer(playerID).toArray(Game[]::new));
        }
        catch(RepositoryException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
