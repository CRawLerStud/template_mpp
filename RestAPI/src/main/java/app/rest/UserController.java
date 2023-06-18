package app.rest;

import app.model.Configuration;
import app.model.User;
import app.persistance.ConfigurationRepository;
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

    @Autowired
    public UserController(UserRepository userRepository, ConfigurationRepository configurationRepository) {
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
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

}
