package app.rest;

import app.model.SecretWord;
import app.persistance.GameRepository;
import app.persistance.SecretWordRepository;
import app.persistance.utils.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/games")
public class GameController {

    private final SecretWordRepository secretWordRepository;
    private final GameRepository gameRepository;

    @Autowired
    public GameController(SecretWordRepository secretWordRepository, GameRepository gameRepository) {
        this.secretWordRepository = secretWordRepository;
        this.gameRepository = gameRepository;
    }

    @RequestMapping(value = "/answers/{gameID}", method = RequestMethod.GET)
    private ResponseEntity<Object> getAnswersForGame(@PathVariable Long gameID){
        try{
            List<SecretWord> words =
                    secretWordRepository.secretWordsForGame(gameID);
            List<String> finalWords = new ArrayList<>();
            for(SecretWord word : words){
                finalWords.add(word.getWord());
            }
            return ResponseEntity.ok(finalWords);
        }
        catch(RepositoryException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
