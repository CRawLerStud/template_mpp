package app.rest;

import app.model.User;
import app.persistance.UserRepository;
import app.persistance.utils.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
