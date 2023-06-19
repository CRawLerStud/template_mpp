package app.rest;

import app.model.Answer;
import app.model.SecretWord;
import app.persistance.AnswerRepository;
import app.persistance.GameRepository;
import app.persistance.SecretWordRepository;
import app.persistance.utils.RepositoryException;
import app.rest.dto.AnswerRestDto;
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
    private final AnswerRepository answerRepository;

    @Autowired
    public GameController(SecretWordRepository secretWordRepository, GameRepository gameRepository,
                          AnswerRepository answerRepository) {
        this.secretWordRepository = secretWordRepository;
        this.gameRepository = gameRepository;
        this.answerRepository = answerRepository;
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

    @RequestMapping(value = "/answers/{gameID}/{playerID}", method = RequestMethod.GET)
    private ResponseEntity<Object> getAnswersForGameAndPlayer(@PathVariable Long gameID, @PathVariable Long playerID){
        try{
            List<Answer> answers = answerRepository.getAnswersForGameAndPlayer(gameID, playerID);
            List<AnswerRestDto> restDtos = new ArrayList<>();
            for(Answer answer : answers){
                AnswerRestDto restDto = new AnswerRestDto();
                restDto.setAnswer(answer.getAnswer());
                restDto.setUsername(answer.getUser().getUsername());
                restDto.setPoints(answer.getPoints());
                restDto.setGameID(answer.getRound().getGame().getId());
                restDtos.add(restDto);
            }
            return ResponseEntity.ok().body(restDtos);
        }
        catch(RepositoryException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
